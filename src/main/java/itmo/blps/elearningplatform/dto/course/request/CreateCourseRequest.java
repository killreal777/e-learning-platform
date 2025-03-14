package itmo.blps.elearningplatform.dto.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreateCourseRequest(

        @Schema(example = "course")
        @JsonProperty(value = "name", required = true)
        String name,

        @JsonProperty(value = "tasks", required = true)
        List<CreateTaskRequest> tasks
) {
}
