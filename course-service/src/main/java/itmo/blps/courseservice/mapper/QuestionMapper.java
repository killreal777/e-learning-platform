package itmo.blps.courseservice.mapper;

import itmo.blps.courseservice.dto.course.QuestionDto;
import itmo.blps.courseservice.dto.course.request.CreateQuestionRequest;
import itmo.blps.courseservice.model.Question;
import org.mapstruct.Mapper;

@Mapper
public interface QuestionMapper extends EntityMapper<QuestionDto, Question> {

    Question toEntity(CreateQuestionRequest request);
}
