package com.roadmaker.member.controller;

import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.authentication.SecurityUtil;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.dto.LoginRequest;
import com.roadmaker.member.dto.SignupRequest;
import com.roadmaker.member.dto.TokenInfo;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.member.service.MemberServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    public final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@Valid @RequestBody SignupRequest request) {
        // 이메일 중복 검사
        if (memberService.isUserRegistered(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        // 비밀번호 암호화
        Member member = new Member(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname());

        // 저장 후 201 status 응답
        memberService.signUp(member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public TokenInfo login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        return memberService.login(email, password);
    }

    @LoginRequired
    @PostMapping("/test")
    public String test(@LoginMember Member member) {
        return member.toString();
    }
}
