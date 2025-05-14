package itmo.blps.courseservice.mapper;

import itmo.blps.courseservice.dto.course.TestAnswerDto;
import itmo.blps.courseservice.model.QuestionAnswer;
import itmo.blps.courseservice.model.TestAnswer;
import org.mapstruct.Mapper;

import java.util.Objects;

@Mapper(uses = {TestMapper.class, QuestionAnswerMapper.class, UserMapper.class})
public interface TestAnswerMapper extends EntityMapper<TestAnswerDto, TestAnswer> {

    default Integer getTotalScore(TestAnswer entity) {
        return entity.getQuestionAnswers().stream()
                .filter(Objects::nonNull)
                .map(QuestionAnswer::getScore)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    default TestAnswerDto toDtoWithTotalScore(TestAnswer entity) {
        return toDto(entity).withTotalScore(getTotalScore(entity));
    }
}
