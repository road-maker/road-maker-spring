package com.roadmaker.member.service;

import com.roadmaker.member.domain.entity.Member;
import com.roadmaker.member.dto.TokenInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface MemberService {
    // 회원가입
    public void signUp(Member member);

    // 로그인
    public TokenInfo login(String email, String password);

    // 중복 가입 검사
    public boolean isUserRegistered(String email);
}
