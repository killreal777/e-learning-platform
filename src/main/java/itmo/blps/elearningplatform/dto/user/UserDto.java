package itmo.blps.elearningplatform.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import itmo.blps.elearningplatform.model.User;

public record UserDto(

        @Schema(example = "1")
        @JsonProperty(value = "id", required = true)
        Integer id,

        @Schema(example = "killreal777")
        @JsonProperty(value = "username", required = true)
        String username,

        @Schema(example = "ROLE_OWNER")
        @JsonProperty(value = "role", required = true)
        User.Role role
) {
}
