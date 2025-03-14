package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.TaskDto;
import itmo.blps.elearningplatform.dto.course.request.CreateTaskRequest;
import itmo.blps.elearningplatform.model.course.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapper extends EntityMapper<TaskDto, Task> {

    Task toEntity(CreateTaskRequest request);
}
