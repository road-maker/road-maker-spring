package com.roadmaker.member.service;

import com.roadmaker.image.dto.UploadImageResponse;
import com.roadmaker.member.dto.MemberResponse;
import com.roadmaker.member.dto.MypageRequest;
import com.roadmaker.member.dto.SignupRequest;
import com.roadmaker.member.dto.TokenInfo;
import com.roadmaker.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public interface MemberService {
    // 회원가입
    public void signUp(SignupRequest signupRequest);

    // 로그인
    public TokenInfo login(String email, String password);

    public UploadImageResponse uploadMemberAvatar(Member member, MultipartFile image) throws IOException;

    public boolean isDuplicatedEmail(String email);

    public boolean isDuplicatedNickname(String nickname);

    public Optional<Member> getLoggedInMember();

    public MemberResponse saveProfile(MypageRequest request, Member member);

    public MemberResponse findMemberByEmail(String email);
    public MemberResponse findMemberByNickname(String nickname);
    public MemberResponse findMemberByMemberId(Long memberId);

}
