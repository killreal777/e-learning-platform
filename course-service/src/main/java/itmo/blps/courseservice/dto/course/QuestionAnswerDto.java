package itmo.blps.courseservice.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record QuestionAnswerDto(

        @JsonProperty(value = "question", required = true)
        QuestionDto question,

        @Schema(example = "1")
        @JsonProperty(value = "selectedOption", required = true)
        Integer selectedOption,

        @Schema(example = "100")
        @JsonProperty(value = "score", required = false)
        Integer score
) {
}
