package itmo.blps.elearningplatform.dto.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreateTestAnswerRequest(

        @Schema(example = "[1, 2, 3]")
        @JsonProperty(value = "selectedOptions", required = true)
        List<Integer> selectedOptions
) {
}
