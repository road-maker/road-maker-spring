package com.roadmaker.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.roadmaker.global.error.ApiException;
import lombok.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class ErrorResponse {
    private final int httpStatus;
    private final String message;
    private final String errorCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    @Builder
    private ErrorResponse(int httpStatus, String message, String errorCode, List<ValidationError> errors) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public static ErrorResponse of(ApiException e) {
        return ErrorResponse.builder()
                .httpStatus(e.getHttpStatus().value())
                .message(e.getMessage())
                .errorCode(e.getErrorCode())
                .build();
    }

    public static ErrorResponse of(ApiException e, BindingResult bindingResult) {
        List<ValidationError> errors = bindingResult.getFieldErrors().stream().map(ValidationError::of).toList();
        return ErrorResponse.builder()
                .httpStatus(e.getHttpStatus().value())
                .message(e.getMessage())
                .errorCode(e.getErrorCode())
                .errors(errors)
                .build();
    }

    @Builder
    public record ValidationError(String field, String message) {
        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}