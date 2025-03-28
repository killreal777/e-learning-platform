package itmo.blps.elearningplatform.dto.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateQuestionRequest(

        @Schema(example = "text")
        @JsonProperty(value = "text", required = true)
        String text,

        @Schema(example = "option1")
        @JsonProperty(value = "option1", required = true)
        String option1,

        @Schema(example = "option2")
        @JsonProperty(value = "option2", required = true)
        String option2,

        @Schema(example = "option3")
        @JsonProperty(value = "option3", required = true)
        String option3,

        @Schema(example = "option4")
        @JsonProperty(value = "option4", required = true)
        String option4,

        @Schema(example = "1")
        @JsonProperty(value = "correctOptionNumber", required = true)
        Integer correctOptionNumber,

        @Schema(example = "100")
        @JsonProperty(value = "maxScore", required = true)
        Integer maxScore
) {
}
