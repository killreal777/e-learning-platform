package itmo.blps.elearningplatform.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CourseDto(

        @Schema(example = "1")
        @JsonProperty(value = "id", required = true)
        Integer id,

        @Schema(example = "course")
        @JsonProperty(value = "name", required = true)
        String name,

        @JsonProperty(value = "tasks", required = true)
        List<TaskDto> tasks
) {
}
