package itmo.blps.elearningplatform.dto.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateHomeworkRequest(

        @Schema(example = "homework")
        @JsonProperty(value = "name", required = true)
        String name,

        @Schema(example = "description")
        @JsonProperty(value = "description", required = true)
        String description,

        @Schema(example = "100")
        @JsonProperty(value = "maxScore", required = true)
        Integer maxScore
) {
}
