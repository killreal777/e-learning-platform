package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.QuestionAnswerDto;
import itmo.blps.elearningplatform.model.QuestionAnswer;
import org.mapstruct.Mapper;

@Mapper(uses = {QuestionMapper.class, TestAnswerMapper.class})
public interface QuestionAnswerMapper extends EntityMapper<QuestionAnswerDto, QuestionAnswer> {
}
