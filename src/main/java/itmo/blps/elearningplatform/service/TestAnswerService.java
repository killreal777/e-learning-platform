package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.dto.course.request.CreateTestAnswerRequest;
import itmo.blps.elearningplatform.mapper.TestAnswerMapper;
import itmo.blps.elearningplatform.model.*;
import itmo.blps.elearningplatform.repository.TestAnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestAnswerService {

    private final TestAnswerRepository testAnswerRepository;
    private final TestAnswerMapper testAnswerMapper;

    private final TestService testService;
    private final StudyService studyService;
    private final MarkService markService;

    private final PlatformTransactionManager transactionManager;

    public Integer getStudentTestsScore(Integer studentId, Integer courseId) {
        return testAnswerRepository
                .findAllByStudentIdAndTestCourseIdAndActualTrue(studentId, courseId)
                .stream()
                .filter(Objects::nonNull)
                .map(testAnswerMapper::getTotalScore)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    public TestAnswerDto completeTest(Integer testId, CreateTestAnswerRequest request, User student) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            log.atInfo().setMessage("Starting transaction").log();
            Test test = testService.getTestEntityById(testId);
            studyService.validateStudentIsEnrolled(student.getId(), test.getCourse().getId());
            TestAnswer testAnswer = new TestAnswer();
            testAnswer.setStudentId(student.getId());
            testAnswer.setTest(test);
            checkAnswers(test, testAnswer, request.selectedOptions());
            updateActual(testAnswer);
            testAnswer = testAnswerRepository.save(testAnswer);
            markService.updateMark(student.getId(), test.getCourse().getId());
            TestAnswerDto testAnswerDto = testAnswerMapper.toDtoWithTotalScore(testAnswer);
            log.atInfo().setMessage("Transaction committed successfully").log();
            transactionManager.commit(status);
            return testAnswerDto;
        } catch (Exception e) {
            log.atError().setMessage("Transaction failed, rolling back").log();
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void checkAnswers(Test test, TestAnswer answer, List<Integer> selectedOptions) {
        List<QuestionAnswer> questionAnswers = IntStream.range(0, test.getQuestions().size()).mapToObj(i -> {
            Question question = test.getQuestions().get(i);
            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setTestAnswer(answer);
            questionAnswer.setQuestion(question);
            questionAnswer.setSelectedOption(selectedOptions.get(i));
            setScore(question, questionAnswer);
            return questionAnswer;
        }).toList();
        answer.setQuestionAnswers(questionAnswers);
    }

    private void updateActual(TestAnswer answer) {
        TestAnswer lastActualAnswer = testAnswerRepository
                .findByTestIdAndStudentIdAndActualTrue(
                        answer.getTest().getId(),
                        answer.getStudentId()
                ).orElse(null);
        int currentAnswerTotalScore = testAnswerMapper.getTotalScore(answer);
        if (lastActualAnswer != null && testAnswerMapper.getTotalScore(lastActualAnswer) <= currentAnswerTotalScore) {
            lastActualAnswer.setActual(false);
            testAnswerRepository.save(lastActualAnswer);
            answer.setActual(true);
        } else if (lastActualAnswer == null) {
            answer.setActual(true);
        }
    }

    private void setScore(Question question, QuestionAnswer answer) {
        if (answer.getSelectedOption() != null && answer.getSelectedOption().equals(question.getCorrectOption())) {
            answer.setScore(question.getMaxScore());
        } else {
            answer.setScore(0);
        }
    }
}
