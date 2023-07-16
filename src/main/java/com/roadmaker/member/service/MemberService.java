package com.roadmaker.member.service;

import com.roadmaker.member.domain.entity.Member;

public interface MemberService {
    // 회원가입
    public void signUp(Member member);

    // 로그인

    // 중복 가입 검사
    public boolean isUserRegistered(String email);
}
