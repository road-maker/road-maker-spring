package com.roadmaker.member.controller;

import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.dto.*;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.dto.RoadmapDto;
import com.roadmaker.roadmap.service.RoadmapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    public final PasswordEncoder passwordEncoder;
    private final RoadmapService roadmapService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@Valid @RequestBody SignupRequest signupRequest) {
        // 이메일 중복 검사
        if (memberService.isUserRegistered(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // 비밀번호 암호화 후 저장
        memberService.signUp(signupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path="/signin")
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

    @GetMapping(path="/{nickname}")
    public ResponseEntity<MypageResponse> gotoMypage(@PathVariable String nickname) {
        //1. 요청 데이터 검증
        Long memberId = memberService.findMemberByNickname(nickname).getId();
        //2. 비즈니스 로직 처리
        MypageResponse mypageResponse = memberService.callMyPage(memberId);
        if ( mypageResponse == null)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<>(mypageResponse, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping(path="/save-profile")
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

    @LoginRequired
    @GetMapping(path="/{nickname}/in-progress-roadmaps")
    public ResponseEntity<List<RoadmapDto>> callRoadmapJoined(@PathVariable String nickname) {
        Long memberId = memberService.findMemberByNickname(nickname).getId();

        //이 멤버가 조인하고 잇는 모든 로드맵을 dto형태로의 리스트로 전달
        List<RoadmapDto> joiningRoadmaps = roadmapService.findRoadmapJoinedByMemberId(memberId);
        return new ResponseEntity<>(joiningRoadmaps, HttpStatus.OK);
    }

    @LoginRequired
    @GetMapping(path="/{nickname}/roadmaps")
    public ResponseEntity<List<RoadmapDto>> callRoadmapCreated(@PathVariable String nickname) {
        Long memberId = memberService.findMemberByNickname(nickname).getId();

        //이 멤버가 만든 모든 로드맵을 dto형태로의 리스트로 전달
        List<RoadmapDto> createdRoadmaps = roadmapService.findRoadmapCreatedByMemberId(memberId);
        return new ResponseEntity<>(createdRoadmaps, HttpStatus.OK);
    }

}
