package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.dto.course.request.CreateTestAnswerRequest;
import itmo.blps.elearningplatform.mapper.TestAnswerMapper;
import itmo.blps.elearningplatform.model.*;
import itmo.blps.elearningplatform.repository.TestAnswerRepository;
import itmo.blps.elearningplatform.service.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

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

    @Transactional
    public TestAnswerDto completeTest(Integer testId, CreateTestAnswerRequest request, User student) {
        Test test = testService.getTestEntityById(testId);
        studyService.validateStudentIsEnrolled(student.getId(), test.getCourse().getId());
        TestAnswer testAnswer = new TestAnswer();
        testAnswer.setStudentId(student.getId());
        testAnswer.setTest(test);
        checkAnswers(test, testAnswer, request.selectedOptions());
        testAnswer = testAnswerRepository.save(testAnswer);
        return testAnswerMapper.toDtoWithTotalScore(testAnswer);
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
