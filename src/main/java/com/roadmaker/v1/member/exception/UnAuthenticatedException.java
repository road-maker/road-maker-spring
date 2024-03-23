package com.roadmaker.v1.member.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class UnAuthenticatedException extends ApiException {
    public UnAuthenticatedException() {
        super(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다. 로그인이 필요합니다.");
    }
}
