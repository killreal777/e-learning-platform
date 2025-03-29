package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.model.QuestionAnswer;
import itmo.blps.elearningplatform.model.TestAnswer;
import org.mapstruct.Mapper;

@Mapper(uses = {TestMapper.class, QuestionAnswerMapper.class, UserMapper.class})
public interface TestAnswerMapper extends EntityMapper<TestAnswerDto, TestAnswer> {

    default Integer getTotalScore(TestAnswer entity) {
        return entity.getQuestionAnswers().stream()
                .map(QuestionAnswer::getScore)
                .reduce(0, Integer::sum);
    }

    default TestAnswerDto toDtoWithTotalScore(TestAnswer entity) {
        return toDto(entity).withTotalScore(getTotalScore(entity));
    }
}
