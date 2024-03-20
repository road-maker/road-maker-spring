package com.roadmaker.auth.service;

import com.roadmaker.member.authentication.JwtProvider;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.member.exception.UnAuthenticatedException;
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

    public Optional<Member> getLoggedInMember() {
        String accessToken = httpServletRequest.getHeader("Authorization");
        boolean result = jwtProvider.validationToken(accessToken);
        if (!result) {
            throw new UnAuthenticatedException();
        }

        String memberId = jwtProvider.extractSubject(accessToken);

        return memberRepository.findById(Long.parseLong(memberId));
    }
}
