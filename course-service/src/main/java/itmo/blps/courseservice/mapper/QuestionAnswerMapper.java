package itmo.blps.courseservice.mapper;

import itmo.blps.courseservice.dto.course.QuestionAnswerDto;
import itmo.blps.courseservice.model.QuestionAnswer;
import org.mapstruct.Mapper;

@Mapper(uses = {QuestionMapper.class, TestAnswerMapper.class})
public interface QuestionAnswerMapper extends EntityMapper<QuestionAnswerDto, QuestionAnswer> {
}
