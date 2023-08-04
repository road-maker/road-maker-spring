package com.roadmaker.commons.exception;

public class ExecutionException extends RuntimeException{
    public ExecutionException() {
        super();
    }

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
