package com.roadmaker.global.error;

import com.roadmaker.global.exception.ConflictException;
import com.roadmaker.global.exception.NotFoundException;
import com.roadmaker.member.exception.UnAuthenticatedException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<HttpStatus> authenticatedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpStatus> notFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<HttpStatus> conflictException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> validationNotValidException(MethodArgumentNotValidException e) {
        List<ValidationError> errors = e.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @Getter
    @Setter
    public class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}