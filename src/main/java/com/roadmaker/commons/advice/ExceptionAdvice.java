package com.roadmaker.commons.advice;

import com.roadmaker.member.exception.UnAuthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<HttpStatus> authenticatedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}