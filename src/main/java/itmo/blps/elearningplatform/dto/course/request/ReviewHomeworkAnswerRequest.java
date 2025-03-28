package itmo.blps.elearningplatform.dto.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewHomeworkAnswerRequest(

        @Schema(example = "100")
        @JsonProperty(value = "score", required = true)
        Integer score
) {
}
