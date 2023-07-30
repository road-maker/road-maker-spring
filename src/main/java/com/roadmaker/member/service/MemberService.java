package com.roadmaker.member.service;

import com.roadmaker.member.dto.*;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.dto.TokenInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface MemberService {
    // 회원가입
    public void signUp(SignupRequest signupRequest);

    // 로그인
    public TokenInfo login(String email, String password);

    public boolean isDuplicatedEmail(String email);

    public boolean isDuplicatedNickname(String nickname);

    public Member getLoggedInMember();

    public MemberResponse saveProfile(MypageRequest request, Member member);

    public MemberResponse findMemberByEmail(String email);
    public MemberResponse findMemberByNickname(String nickname);

}
