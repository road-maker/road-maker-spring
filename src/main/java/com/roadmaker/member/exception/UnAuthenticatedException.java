package com.roadmaker.member.exception;

public class UnAuthenticatedException extends RuntimeException {
    public UnAuthenticatedException() {
        super();
    }

    public UnAuthenticatedException(String message) {
        super(message);
    }

    public UnAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
