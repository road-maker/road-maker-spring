package com.roadmaker.member.controller;

import com.roadmaker.comment.dto.CommentResponse;
import com.roadmaker.comment.service.CommentService;
import com.roadmaker.global.annotation.LoginMember;
import com.roadmaker.global.annotation.LoginRequired;
import com.roadmaker.global.exception.ConflictException;
import com.roadmaker.global.exception.NotFoundException;
import com.roadmaker.image.dto.UploadImageResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupRequest) {
        // 이메일 중복 검사
        if (memberService.isDuplicatedEmail(signupRequest.getEmail()) && memberService.isDuplicatedNickname(signupRequest.getNickname())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("이메일과 닉네임 중복");
        }
        else if (memberService.isDuplicatedEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이메일 중복");
        }
        // 닉네임 중복 검사
        else if(memberService.isDuplicatedNickname(signupRequest.getNickname())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("닉네임 중복");
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

    @LoginRequired
    @PostMapping("/me/avatar")
    public ResponseEntity<UploadImageResponse> uploadMemberAvatar(@RequestPart(value = "file") MultipartFile multipartFile, @LoginMember Member member) throws IOException {
        UploadImageResponse uploadImageResponse = memberService.uploadMemberAvatar(member, multipartFile);
        return new ResponseEntity<>(uploadImageResponse, HttpStatus.CREATED);
    }


    @GetMapping(path="/{memberId}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long memberId) {
        MemberResponse memberResponse = memberService.findMemberByMemberId(memberId);
        if (memberResponse == null) {
            throw new NotFoundException("해당 멤버를 찾지 못함");
        }
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @GetMapping(path="/{memberId}/comments")
    public ResponseEntity<CommentResponse> findMemberComments(@PathVariable Long memberId,@RequestParam(name="page") Integer page) {
        int size = 8;

        if(memberId == null) { throw new NotFoundException("해당 멤버를 찾지 못함"); }

        CommentResponse commentsInPage = commentService.findByMemberIdAndPageRequest(memberId,page, size);
        return new ResponseEntity<>(commentsInPage, HttpStatus.OK);
    }

    @LoginRequired
    @PatchMapping(path="/save-profile")
    public ResponseEntity<MemberResponse> changeProfile(@Valid @RequestBody MypageRequest request, @LoginMember Member member) {
        //1. 내 닉네임 안 바꾸는 경우 예외 처리 -> saveprofile에서.
        //2. 내가 넣으려는 닉네임이 중복되는 경우 예외 409처리
        //2. 비즈니스 로직 처리
        MemberResponse memberResponse = memberService.saveProfile(request, member);
        if(memberResponse!=null) {
            //3. 응답 메세지 처리
            return new ResponseEntity<>(memberResponse, HttpStatus.CREATED); //
        } else {
            throw new ConflictException();
        }
    }

    @LoginRequired
    @GetMapping(path="/{nickname}/in-progress-roadmaps")
    public ResponseEntity<List<RoadmapDto>> callRoadmapJoined(@PathVariable String nickname) {
        Long memberId = memberService.findMemberByNickname(nickname).getId();
        //해당 멤버를 찾을 수 없는경우: 발생할 일 없음

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
