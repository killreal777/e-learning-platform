package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.dto.course.request.CreateTestAnswerRequest;
import itmo.blps.elearningplatform.mapper.TestAnswerMapper;
import itmo.blps.elearningplatform.model.*;
import itmo.blps.elearningplatform.repository.TestAnswerRepository;
import itmo.blps.elearningplatform.service.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Integer getStudentTestsScore(Integer studentId, Integer courseId) {
        return testAnswerRepository
                .findAllByStudentIdAndTestCourseIdAndActualTrue(studentId, courseId)
                .stream()
                .filter(Objects::nonNull)
                .map(testAnswerMapper::getTotalScore)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    public void startTest(Integer testId, User student) {
        Test test = testService.getTestEntityById(testId);
        studyService.validateStudentIsEnrolled(student.getId(), test.getCourse().getId());
        testAnswerRepository.findByTestIdAndStudentIdAndEndTimeIsNull(testId, student.getId()).ifPresent(testAnswer -> {
            String message = "Previous test attempt has not been finished yet";
            log.atError()
                    .setMessage(message)
                    .addKeyValue("testId", testId)
                    .addKeyValue("studentId", student.getId())
                    .addKeyValue("testAnswerId", testAnswer.getId())
                    .log();
            throw new IllegalStateException(message);
        });
        TestAnswer testAnswer = new TestAnswer();
        testAnswer.setStudentId(student.getId());
        testAnswer.setTest(test);
        testAnswerRepository.save(testAnswer);
    }

    public TestAnswerDto completeTest(Integer testId, CreateTestAnswerRequest request, User student) {
        TestAnswer testAnswer = testAnswerRepository.findByTestIdAndStudentIdAndEndTimeIsNull(testId, student.getId())
                .orElseThrow(() -> {
                    String message = "There is no started test attempt";
                    log.atError()
                            .setMessage(message)
                            .addKeyValue("testId", testId)
                            .addKeyValue("studentId", student.getId())
                            .log();
                    return new IllegalStateException(message);
                });
        testAnswer.setEndTime(LocalDateTime.now());
        Test test = testService.getTestEntityById(testId);
        checkAnswers(test, testAnswer, request.selectedOptions());
        updateActual(testAnswer);
        testAnswer = testAnswerRepository.save(testAnswer);
        return testAnswerMapper.toDtoWithTotalScore(testAnswer);
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

    private void setScore(Question question, QuestionAnswer answer) {
        if (answer.getSelectedOption() != null && answer.getSelectedOption().equals(question.getCorrectOption())) {
            answer.setScore(question.getMaxScore());
        } else {
            answer.setScore(0);
        }
    }
}
