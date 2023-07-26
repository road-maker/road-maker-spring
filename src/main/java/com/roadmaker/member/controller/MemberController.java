package com.roadmaker.member.controller;

import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.authentication.SecurityUtil;
import com.roadmaker.member.dto.*;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.member.service.MemberServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        TokenInfo tokenInfo = memberService.login(email, password);
        MemberResponse member = memberService.findMemberByEmail(email);

        LoginResponse loginResponse = LoginResponse.builder()
                .member(member)
                .tokenInfo(tokenInfo)
                .build();

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/test")
    public String test(@LoginMember Member member) {
        return member.toString();
    }

    @LoginRequired
    @GetMapping("/{nickname}")
    public MypageResponse gotoMypage(@PathVariable String nickname) { //email
        //1. 요청 데이터 검증
        //2. 비즈니스 로직 처리
        MypageResponse mypageResponse = memberService.callMyPage(nickname);
        if ( mypageResponse == null)
        {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        ResponseEntity.status(HttpStatus.ACCEPTED).build();
        return mypageResponse;
    }

    @LoginRequired
    @PostMapping("/save-profile")
    public ResponseEntity<HttpStatus> changeProfile(@Valid @RequestBody MypageRequest request, @LoginMember Member member) {
        //1. 요청 데이터 검증
        if (request.getNickname() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        //2. 비즈니스 로직 처리
        if(memberService.saveProfile(request, member).equals(true)) {
            //3. 응답 메세지 처리
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
