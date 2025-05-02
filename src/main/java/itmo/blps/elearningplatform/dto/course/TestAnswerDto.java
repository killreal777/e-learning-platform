package itmo.blps.elearningplatform.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record TestAnswerDto(

        @JsonProperty(value = "id", required = true)
        Integer id,

        @JsonProperty(value = "test", required = true)
        TestDto test,

        @JsonProperty(value = "questionAnswers", required = true)
        List<QuestionAnswerDto> questionAnswers,

        @Schema(example = "true")
        @JsonProperty(value = "actual", required = true)
        Boolean actual,

        @JsonProperty(value = "totalScore", required = true)
        Integer totalScore
) {
}
