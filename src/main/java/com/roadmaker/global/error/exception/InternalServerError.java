package com.roadmaker.global.error.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class InternalServerError extends ApiException {
    public InternalServerError() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류");
    }
}
