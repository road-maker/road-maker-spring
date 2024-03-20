package com.roadmaker.member.service;

import com.roadmaker.member.authentication.JwtProvider;
import com.roadmaker.image.dto.UploadImageResponse;
import com.roadmaker.image.service.ImageService;
import com.roadmaker.member.dto.response.MemberLoginResponse;
import com.roadmaker.member.dto.response.MemberResponse;
import com.roadmaker.member.dto.request.MemberUpdateRequest;
import com.roadmaker.member.dto.request.MemberSignupRequest;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.member.exception.EmailAlreadyRegisteredException;
import com.roadmaker.member.exception.MemberNotFoundException;
import com.roadmaker.member.exception.NicknameAlreadyRegisteredException;
import com.roadmaker.member.exception.UnAuthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest httpServletRequest;
    private final ImageService imageService;

    @Transactional
    public void signUp(MemberSignupRequest request) {
        if (isDuplicatedEmail(request.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }
        if (isDuplicatedNickname(request.getNickname())) {
            throw new NicknameAlreadyRegisteredException();
        }

        Member member = request.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    @Transactional
    public MemberLoginResponse login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        boolean isMatched = passwordEncoder.matches(password, member.getPassword());
        if (!isMatched) {
            throw new UnAuthenticatedException();
        }

        String accessToken = jwtProvider.generate(member.getId().toString(), new Date((new Date()).getTime() + 1000 * 60 * 60 * 24));

        return MemberLoginResponse.of(member, accessToken);
    }

    @Transactional
    public UploadImageResponse uploadMemberAvatar(Member member, MultipartFile image) throws IOException {
        String imageUrl = imageService.uploadImage(image);
        member.setAvatarUrl(imageUrl);

        return UploadImageResponse.builder().url(imageUrl).build();
    }

    @Transactional
    public MemberResponse saveProfile(MemberUpdateRequest request, Member member) {
        //1. 내가 입력한 닉네임이 이미 내 닉네임과 동일한 경우 충돌 피하기 위함
        if (!request.getNickname().equals(member.getNickname())) {
            //2. 다른 동일한 닉네임이 존재할 경우 409리턴하도록
            if (isDuplicatedNickname(request.getNickname())) {
                throw new NicknameAlreadyRegisteredException();
            }
            member.setNickname(request.getNickname());
        }

        member.setBio(request.getBio());
        member.setBaekjoonId(request.getBaekjoonId());
        member.setBlogUrl(request.getBlogUrl());

        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    private boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    private boolean isDuplicatedNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }
}
