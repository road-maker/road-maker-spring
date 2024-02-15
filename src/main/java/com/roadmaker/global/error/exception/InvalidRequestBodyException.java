package com.roadmaker.global.error.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidRequestBodyException extends ApiException {
    public InvalidRequestBodyException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
