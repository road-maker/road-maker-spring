package com.roadmaker.v1.member.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class NicknameAlreadyRegisteredException extends ApiException {
    public NicknameAlreadyRegisteredException() {
        super(HttpStatus.CONFLICT, "이미 등록된 닉네임입니다.");
    }
}
