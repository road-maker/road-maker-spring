package com.roadmaker.v1.auth.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class LoginFailedException  extends ApiException {
    public LoginFailedException() {
        super(HttpStatus.UNAUTHORIZED, "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.");
    }
}
