package itmo.blps.courseservice.dto.course.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreateTestRequest(

        @Schema(example = "test")
        @JsonProperty(value = "name", required = true)
        String name,

        @Schema(example = "description")
        @JsonProperty(value = "description", required = true)
        String description,

        @JsonProperty(value = "questions", required = true)
        List<CreateQuestionRequest> questions
) {
}
