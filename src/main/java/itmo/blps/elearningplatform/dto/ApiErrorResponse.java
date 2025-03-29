package itmo.blps.elearningplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ApiErrorResponse(

        @JsonProperty(value = "description", required = true)
        String description,

        @JsonProperty(value = "code", required = true)
        String code,

        @JsonProperty(value = "exceptionName", required = true)
        String exceptionName,

        @JsonProperty(value = "exceptionMessage", required = true)
        String exceptionMessage,

        @JsonProperty(value = "stacktrace", required = true)
        List<String> stacktrace
) {
}

