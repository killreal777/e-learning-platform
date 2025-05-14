package itmo.blps.courseservice.mapper;

import itmo.blps.courseservice.dto.course.HomeworkDto;
import itmo.blps.courseservice.dto.course.request.CreateHomeworkRequest;
import itmo.blps.courseservice.model.Homework;
import org.mapstruct.Mapper;

@Mapper
public interface HomeworkMapper extends EntityMapper<HomeworkDto, Homework> {

    Homework toEntity(CreateHomeworkRequest request);
}
