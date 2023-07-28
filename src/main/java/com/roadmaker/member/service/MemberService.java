package com.roadmaker.member.service;

import com.roadmaker.member.dto.MemberResponse;
import com.roadmaker.member.dto.MypageRequest;
import com.roadmaker.member.dto.MypageResponse;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.dto.TokenInfo;
import com.roadmaker.roadmap.dto.RoadmapResponse;
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

    public Member getLoggedInMember();

    public MypageResponse callMyPage(Long memberId);
    public Boolean saveProfile(MypageRequest request, Member member);

    public MemberResponse findMemberByEmail(String email);
    public MemberResponse findMemberByNickname(String nickname);

}
