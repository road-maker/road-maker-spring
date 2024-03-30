package com.roadmaker.v1.auth.service;

import com.roadmaker.v1.auth.dto.request.AuthLoginRequest;
import com.roadmaker.v1.auth.dto.request.AuthSignupRequest;
import com.roadmaker.v1.auth.dto.response.AuthLoginResponse;
import com.roadmaker.v1.auth.dto.response.AuthSignupResponse;
import com.roadmaker.v1.member.authentication.JwtProvider;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.entity.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final HttpServletRequest httpServletRequest;
    private final JwtProvider jwtProvider;

    public AuthSignupResponse signup(AuthSignupRequest request) {
        return new AuthSignupResponse(1L, "accesstoken");
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        return new AuthLoginResponse(1L, "accesstokekn");
    }

    public Optional<Member> getLoggedInMember() {
        String auth = httpServletRequest.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer")) {
            return Optional.empty();
        }

        String accessToken = auth.split(" ")[1].trim();

        boolean result = jwtProvider.validationToken(accessToken);

        if (!result) {
            return Optional.empty();
        }

        String memberId = jwtProvider.extractSubject(accessToken);

        return memberRepository.findById(Long.parseLong(memberId));
    }
}
