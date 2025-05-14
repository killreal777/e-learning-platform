package itmo.blps.courseservice.mapper;

import itmo.blps.courseservice.dto.course.TestDto;
import itmo.blps.courseservice.dto.course.request.CreateTestRequest;
import itmo.blps.courseservice.model.Question;
import itmo.blps.courseservice.model.Test;
import org.mapstruct.Mapper;

@Mapper(uses = {QuestionMapper.class})
public interface TestMapper extends EntityMapper<TestDto, Test> {

    Test toEntity(CreateTestRequest request);

    static void linkEntities(Test test) {
        test.getQuestions().forEach(question -> question.setTest(test));
    }

    default Integer getMaxScore(Test test) {
        return test.getQuestions().stream()
                .map(Question::getMaxScore)
                .reduce(0, Integer::sum);
    }
}
