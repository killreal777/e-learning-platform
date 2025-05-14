package itmo.blps.courseservice.dto.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateHomeworkAnswerRequest(

        @Schema(example = "text")
        @JsonProperty(value = "text", required = true)
        String text
) {
}
