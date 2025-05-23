package itmo.blps.elearningplatform.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationRequest(

        @Schema(example = "killreal777")
        @JsonProperty(value = "username", required = true)
        String username,

        @Schema(example = "qwerty12345")
        @JsonProperty(value = "password", required = true)
        String password
) {
}
