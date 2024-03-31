package com.roadmaker.v1.auth.service;

import com.roadmaker.v1.auth.dto.request.AuthLoginRequest;
import com.roadmaker.v1.auth.dto.request.AuthSignupRequest;
import com.roadmaker.v1.auth.dto.response.AuthLoginResponse;
import com.roadmaker.v1.auth.dto.response.AuthSignupResponse;
import com.roadmaker.v1.auth.exception.LoginFailedException;
import com.roadmaker.v1.member.authentication.JwtProvider;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.entity.MemberRepository;
import com.roadmaker.v1.member.exception.EmailAlreadyRegisteredException;
import com.roadmaker.v1.member.exception.NicknameAlreadyRegisteredException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final HttpServletRequest httpServletRequest;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthSignupResponse signup(AuthSignupRequest request) {
        checkEmailDuplicate(request.email());
        checkNicknameDuplicate(request.nickname());

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .build();

        Member savedMember = memberRepository.save(member);

        String accessToken = generateAccessToken(savedMember);

        return AuthSignupResponse.of(savedMember.getId(), accessToken);
    }


    @Transactional
    public AuthLoginResponse login(AuthLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(LoginFailedException::new);

        checkPasswordMatch(request.password(), member.getPassword());

        String accessToken = generateAccessToken(member);

        return AuthLoginResponse.of(member.getId(), accessToken);
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

    private void checkEmailDuplicate(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    private void checkNicknameDuplicate(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new NicknameAlreadyRegisteredException();
        }
    }

    private String generateAccessToken(Member member) {
        Date oneDayAfter = new Date((new Date()).getTime() + 1000 * 60 * 60 * 24);
        return jwtProvider.generate(member.getId().toString(), oneDayAfter);
    }

    private void checkPasswordMatch(String rawPassword, String encodedPassword) {
        boolean isMatched = passwordEncoder.matches(rawPassword, encodedPassword);
        if(!isMatched) {
            throw new LoginFailedException();
        }
    }
}
