package com.roadmaker.member.service;

import com.roadmaker.member.authentication.JwtProvider;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.member.dto.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public void signUp(Member member) {
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public TokenInfo login(String email, String password) {
        // 로그인 ID/PW를 기반으로 authentication객체 생성
        // authentication은 인증 여부를 확인하는 authenticated 값이 false
        log.info("passed elements -> email: {} password: {}", email, password);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(email, password);
        //검증: 비밀번호 체크. authenticate 메서드가 실행될 때,
        //CustomUserDetailsService에서 만든 loadByUsername실행
        log.info("password check ready -> authenticationToken: {}", authenticationToken);
        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //인증 정보 기반으로 JWT 토큰 생성
        log.info("password check ok -> authentication: {}", authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public boolean isUserRegistered(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }
}
