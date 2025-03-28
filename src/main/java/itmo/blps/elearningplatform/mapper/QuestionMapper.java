package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.QuestionDto;
import itmo.blps.elearningplatform.dto.course.request.CreateQuestionRequest;
import itmo.blps.elearningplatform.model.Question;
import org.mapstruct.Mapper;

@Mapper
public interface QuestionMapper extends EntityMapper<QuestionDto, Question> {

    Question toEntity(CreateQuestionRequest request);
}
