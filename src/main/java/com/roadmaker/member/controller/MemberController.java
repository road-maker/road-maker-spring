package com.roadmaker.member.controller;

import com.roadmaker.member.authentication.SecurityUtil;
import com.roadmaker.member.dto.*;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.service.MemberService;
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
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .level(1)
                .exp(0)
                .build();
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

    @PostMapping("/test")
    public String test() {
        Member member = memberService.getLoggedInMember();
        List<String> memberDetail = new ArrayList<>();
        memberDetail.add(String.valueOf(member.getId()));
        memberDetail.add(member.getUsername());
        memberDetail.add(member.getNickname());
        return memberDetail.toString();
    }

    @GetMapping("/{memberId}")
    public MypageResponse gotoMypage(@PathVariable Long memberId) { //email
        //1. 요청 데이터 검증
        if(!memberId.equals((memberService.getLoggedInMember()).getId())) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        }
        //2. 비즈니스 로직 처리
        MypageResponse mypageResponse = memberService.callMyPage(memberId);
        if ( mypageResponse == null)
        {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        ResponseEntity.status(HttpStatus.ACCEPTED).build();
        return mypageResponse;
    }

    @PostMapping("/save-profile")
    public ResponseEntity<HttpStatus> changeProfile(@RequestBody MypageRequest request) {
        //1. 요청 데이터 검증
        if (request.getNickname() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        //2. 비즈니스 로직 처리
        if(memberService.saveProfile(request).equals(true)) {
            //3. 응답 메세지 처리
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
