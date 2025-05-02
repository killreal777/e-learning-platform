package itmo.blps.elearningplatform.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.blps.elearningplatform.dto.user.UserDto;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@With
public record TestAnswerDto(

        @JsonProperty(value = "id", required = true)
        Integer id,

        @JsonProperty(value = "student", required = true)
        UserDto student,

        @JsonProperty(value = "test", required = true)
        TestDto test,

        @JsonProperty(value = "questionAnswers", required = true)
        List<QuestionAnswerDto> questionAnswers,

        @JsonProperty(value = "startTime", required = true)
        LocalDateTime startTime,

        @JsonProperty(value = "endTime", required = false)
        LocalDateTime endTime,

        @Schema(example = "true")
        @JsonProperty(value = "actual", required = true)
        Boolean actual,

        @JsonProperty(value = "totalScore", required = true)
        Integer totalScore
) {
}
