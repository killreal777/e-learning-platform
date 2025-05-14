package itmo.blps.courseservice.dto.course;

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

        @JsonProperty(value = "homeworks", required = true)
        List<HomeworkDto> homeworks,

        @JsonProperty(value = "tests", required = true)
        List<TestDto> tests
) {
}
