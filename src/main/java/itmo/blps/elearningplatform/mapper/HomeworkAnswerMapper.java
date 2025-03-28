package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.HomeworkAnswerDto;
import itmo.blps.elearningplatform.model.HomeworkAnswer;
import org.mapstruct.Mapper;

@Mapper(uses = {HomeworkMapper.class, UserMapper.class})
public interface HomeworkAnswerMapper extends EntityMapper<HomeworkAnswerDto, HomeworkAnswer> {
}
