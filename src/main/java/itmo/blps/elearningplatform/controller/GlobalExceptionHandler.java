package itmo.blps.elearningplatform.controller;

import itmo.blps.elearningplatform.dto.ApiErrorResponse;
import itmo.blps.elearningplatform.service.exception.StudentIsNotEnrolledException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiErrorResponse handleException(EntityNotFoundException e) {
        return createApiErrorResponse(e, HttpStatus.NOT_FOUND, "Entity not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StudentIsNotEnrolledException.class)
    public ApiErrorResponse handleException(StudentIsNotEnrolledException e) {
        return createApiErrorResponse(e, HttpStatus.BAD_REQUEST, "Student is not enrolled");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleException(Exception e) {
        return createApiErrorResponse(e, HttpStatus.BAD_REQUEST, "Something went wrong");
    }

    private ApiErrorResponse createApiErrorResponse(Exception e, HttpStatus status, String message) {
        e.printStackTrace();
        ApiErrorResponse response = new ApiErrorResponse(
                message,
                Integer.toString(status.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(Objects::toString).toList());
        log.atError()
                .setMessage("API error response")
                .addKeyValue("response", response)
                .log();
        return response;
    }
}
