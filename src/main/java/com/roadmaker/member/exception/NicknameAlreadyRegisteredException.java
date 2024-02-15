package com.roadmaker.member.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class NicknameAlreadyRegisteredException extends ApiException {
    public NicknameAlreadyRegisteredException() {
        super(HttpStatus.BAD_REQUEST, "이미 등록된 닉네임입니다.");
    }
}
