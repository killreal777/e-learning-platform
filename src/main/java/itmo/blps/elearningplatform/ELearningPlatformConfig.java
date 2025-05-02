package itmo.blps.elearningplatform;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ELearningPlatformConfig(
        SecurityConfig security
) {

    public record SecurityConfig(
            JwtConfig jwt,
            @NotNull @DefaultValue("users_data.xml") String usersDataPath
    ) {

        public record JwtConfig(
                @NotNull String secretKey,
                @NotNull @DefaultValue("86400000") Long expiration // a day
        ) {
        }
    }
}
