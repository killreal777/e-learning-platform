package itmo.blps.elearningplatform.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import itmo.blps.elearningplatform.dto.user.UserDto;

import java.util.List;

public record TestAnswerDto(

        @JsonProperty(value = "id", required = true)
        Integer id,

        @JsonProperty(value = "student", required = true)
        UserDto student,

        @JsonProperty(value = "test", required = true)
        TestDto test,

        @JsonProperty(value = "questionAnswers", required = true)
        List<QuestionAnswerDto> questionAnswers
) {
}
