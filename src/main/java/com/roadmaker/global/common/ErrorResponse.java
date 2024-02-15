package com.roadmaker.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.roadmaker.global.error.ApiException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private final int httpStatus;
    private final String message;
    private final String errorCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    public static ErrorResponse of(ApiException e) {
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage(), e.getErrorCode());
    }

    public static ErrorResponse of(ApiException e, BindingResult bindingResult) {
        List<ValidationError> errors = bindingResult.getFieldErrors().stream().map(ValidationError::of).toList();
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage(), e.getErrorCode(), errors);
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