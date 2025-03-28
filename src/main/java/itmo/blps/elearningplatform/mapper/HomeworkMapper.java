package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.HomeworkDto;
import itmo.blps.elearningplatform.dto.course.request.CreateHomeworkRequest;
import itmo.blps.elearningplatform.model.Homework;
import org.mapstruct.Mapper;

@Mapper
public interface HomeworkMapper extends EntityMapper<HomeworkDto, Homework> {

    Homework toEntity(CreateHomeworkRequest request);
}
