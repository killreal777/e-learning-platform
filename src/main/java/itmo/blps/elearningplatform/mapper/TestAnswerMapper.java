package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.model.TestAnswer;
import org.mapstruct.Mapper;

@Mapper(uses = {TestMapper.class, QuestionAnswerMapper.class, UserMapper.class})
public interface TestAnswerMapper extends EntityMapper<TestAnswerDto, TestAnswer> {
}
