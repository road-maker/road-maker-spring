package com.roadmaker.member.controller;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.comment.service.CommentService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final CommentService commentService;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@Valid @RequestBody SignupRequest signupRequest) {
        // 이메일 중복 검사
        if (memberService.isDuplicatedEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // 닉네임 중복 검사
        if(memberService.isDuplicatedNickname(signupRequest.getNickname())) {
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
    public ResponseEntity<MemberResponse> findMember(@PathVariable String nickname) {
        MemberResponse memberResponse = memberService.findMemberByNickname(nickname);
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @GetMapping(path="/{nickname}/comments")
    public ResponseEntity<List<CommentDto>> findMemberComments(@PathVariable String nickname, Integer size, Integer page) {

        Long memberId = memberService.findMemberByNickname(nickname).getId();

        if(memberId == null) { return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); }

        List<CommentDto> commentsInPage = commentService.findByMemberIdAndPageRequest(memberId,page, size);
        return new ResponseEntity<>(commentsInPage, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping(path="/save-profile")
    public ResponseEntity<HttpStatus> changeProfile(@Valid @RequestBody MypageRequest request, @LoginMember Member member) {
//        //2. 비즈니스 로직 처리
//        if(memberService.saveProfile(request, member).equals(true)) {
//            return ResponseEntity.status(HttpStatus.CREATED).build();
//        }
//        return ResponseEntity.status(HttpStatus.CONFLICT).build();

        //1. 내 닉네임 안 바꾸는 경우 예외 처리 -> saveprofile에서.
        //2. 내가 넣으려는 닉네임이 중복되는 경우 예외 409처리
        if (request.getNickname().equals(member.getNickname())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        //2. 비즈니스 로직 처리
        if(Boolean.TRUE.equals(memberService.saveProfile(request, member))) {
            //3. 응답 메세지 처리
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
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
