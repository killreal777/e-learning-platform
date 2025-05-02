package itmo.blps.elearningplatform.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TestDto(

        @Schema(example = "1")
        @JsonProperty(value = "id", required = true)
        Integer id,

        @Schema(example = "test")
        @JsonProperty(value = "name", required = true)
        String name,

        @Schema(example = "description")
        @JsonProperty(value = "description", required = true)
        String description,

        @JsonProperty(value = "questions", required = true)
        List<QuestionDto> questions,

        @Schema(example = "30")
        @JsonProperty(value = "timeLimitSeconds", required = false)
        Integer timeLimitSeconds
) {
}
