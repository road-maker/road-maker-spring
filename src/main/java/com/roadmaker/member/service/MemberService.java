package com.roadmaker.member.service;

import com.roadmaker.member.dto.*;
import com.roadmaker.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface MemberService {
    // 회원가입
    public void signUp(SignupRequest signupRequest);

    // 로그인
    public TokenInfo login(String email, String password);

    // 중복 가입 검사
    public boolean isUserRegistered(String email);

    public Member getLoggedInMember();

    public MypageResponse callMyPage(String nickname);
    public Boolean saveProfile(MypageRequest request, Member member);

    public MemberResponse findMemberByEmail(String email);
}
