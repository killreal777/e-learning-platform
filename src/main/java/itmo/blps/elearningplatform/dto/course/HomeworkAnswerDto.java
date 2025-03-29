package itmo.blps.elearningplatform.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.blps.elearningplatform.dto.user.UserDto;
import itmo.blps.elearningplatform.model.HomeworkAnswer;

public record HomeworkAnswerDto(

        @JsonProperty(value = "id", required = true)
        Integer id,

        @JsonProperty(value = "student", required = true)
        UserDto student,

        @JsonProperty(value = "homework", required = true)
        HomeworkDto homework,

        @Schema(example = "text")
        @JsonProperty(value = "text", required = true)
        String text,

        @JsonProperty(value = "reviewer", required = false)
        UserDto reviewer,

        @Schema(example = "SENT")
        @JsonProperty(value = "status", required = true)
        HomeworkAnswer.Status status,

        @Schema(example = "100")
        @JsonProperty(value = "score", required = false)
        Integer score,

        @Schema(example = "true")
        @JsonProperty(value = "actual", required = true)
        Boolean actual
) {
}
