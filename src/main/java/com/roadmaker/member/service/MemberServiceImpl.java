package com.roadmaker.member.service;

import com.roadmaker.member.authentication.JwtProvider;
import com.roadmaker.member.dto.*;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.member.dto.TokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest httpServletRequest;

    @Override
    @Transactional
    public void signUp(SignupRequest signupRequest) {
        Member member = signupRequest.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public TokenInfo login(String email, String password) {
        // 로그인 ID/PW를 기반으로 authentication객체 생성
        // authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(email, password);
        //검증: 비밀번호 체크. authenticate 메서드가 실행될 때,
        //CustomUserDetailsService에서 만든 loadByUsername실행
        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //인증 정보 기반으로 JWT 토큰 생성
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public boolean isDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }

    @Override
    public boolean isDuplicatedNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        return member.isPresent();
    }

    public Optional<Member> getLoggedInMember() {
        String token = jwtProvider.resolveToken((HttpServletRequest) httpServletRequest);

        if (token == null || !jwtProvider.validationToken(token)) {
            return Optional.empty();
        }


        Authentication authentication = jwtProvider.getAuthentication(token);
        String email = authentication.getName();

        return memberRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public MemberResponse saveProfile( MypageRequest request, Member member) {
        //1. 내가 입력한 닉네임이 이미 내 닉네임과 동일한 경우 충돌 피하기 위함
        if(request.getNickname().equals(member.getNickname())) {
        } else {
            //2. 다른 동일한 닉네임이 존재할 경우 409리턴하도록
            if(memberRepository.findByNickname(request.getNickname()).orElse(null) != null) {
                return null;
            } else {
                member.setNickname(request.getNickname());
            }
        }
        member.setBio(request.getBio());
        member.setBaekjoonId(request.getBaekjoonId());
        member.setBlogUrl(request.getBlogUrl());
//        member.setGithubUrl(request.getGithubUrl());
        memberRepository.save(member);

        return MemberResponse.of(member);
    }

    @Override
    public MemberResponse findMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isEmpty()) {
            return null;
        }

        return MemberResponse.of(member.get());
    }

    public MemberResponse findMemberByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElse(null);
        if(member==null) {
            return null;
        }
        return MemberResponse.of(member);
    }
}
