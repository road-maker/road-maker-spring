package com.roadmaker.member.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyRegisteredException extends ApiException {
    public EmailAlreadyRegisteredException() {
        super(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다.");
    }
}
