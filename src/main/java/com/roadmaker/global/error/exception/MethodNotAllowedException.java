package com.roadmaker.global.error.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class MethodNotAllowedException extends ApiException {
    public MethodNotAllowedException() {
        super(HttpStatus.METHOD_NOT_ALLOWED, "요청한 HTTP 메소드는 이 리소스에서 지원하지 않습니다.");
    }
}
