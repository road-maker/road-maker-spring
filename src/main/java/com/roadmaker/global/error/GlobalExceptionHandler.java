package com.roadmaker.global.error;

import com.roadmaker.global.common.ErrorResponse;
import com.roadmaker.global.error.exception.InternalServerError;
import com.roadmaker.global.error.exception.InvalidRequestBodyException;
import com.roadmaker.global.error.exception.NotFoundException;
import com.roadmaker.member.exception.UnAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        log.error("handleApiException", e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.of(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        log.error("handleUnexpectedException", e);
        InternalServerError ise = new InternalServerError();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(ise));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpStatus> notFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        InvalidRequestBodyException ie = new InvalidRequestBodyException("유효하지 않은 Request Body 입니다.");
        return ResponseEntity.status(e.getStatusCode()).body(ErrorResponse.of(ie, e.getBindingResult()));
    }
}