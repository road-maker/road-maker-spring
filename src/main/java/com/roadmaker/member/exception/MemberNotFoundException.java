package com.roadmaker.member.exception;

import com.roadmaker.global.error.ApiException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends ApiException {
    public MemberNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다.");
    }
}
