package com.roadmaker.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;
    private final String errorCode;

    protected ApiException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errorCode = this.getClass().getSimpleName().replace("Exception", "");
    }
}
