package itmo.blps.courseservice.mapper;

import itmo.blps.courseservice.dto.course.HomeworkAnswerDto;
import itmo.blps.courseservice.model.HomeworkAnswer;
import org.mapstruct.Mapper;

@Mapper(uses = {HomeworkMapper.class, UserMapper.class})
public interface HomeworkAnswerMapper extends EntityMapper<HomeworkAnswerDto, HomeworkAnswer> {
}
