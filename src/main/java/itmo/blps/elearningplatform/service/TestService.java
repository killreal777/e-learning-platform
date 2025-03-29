package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.dto.course.request.CreateTestAnswerRequest;
import itmo.blps.elearningplatform.mapper.TestAnswerMapper;
import itmo.blps.elearningplatform.mapper.TestMapper;
import itmo.blps.elearningplatform.model.*;
import itmo.blps.elearningplatform.repository.TestAnswerRepository;
import itmo.blps.elearningplatform.repository.TestRepository;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final TestAnswerRepository testAnswerRepository;

    private final TestMapper testMapper;
    private final TestAnswerMapper testAnswerMapper;

    public TestAnswerDto completeTest(Integer testId, CreateTestAnswerRequest request, User student) {
        Test test = getTestEntityById(testId);
        TestAnswer testAnswer = new TestAnswer();
        testAnswer.setStudent(student);
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

    private Test getTestEntityById(Integer id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Test.class, id));
    }
}
