package com.roadmaker.v1.member.controller;

import com.roadmaker.v1.comment.dto.response.CommentResponse;
import com.roadmaker.v1.comment.service.CommentService;
import com.roadmaker.global.annotation.LoginMember;
import com.roadmaker.global.annotation.LoginRequired;
import com.roadmaker.v1.image.service.ImageService;
import com.roadmaker.v1.member.dto.request.MemberUpdateRequest;
import com.roadmaker.v1.member.dto.response.MemberFindResponse;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.member.service.MemberAvatarUpdateResponse;
import com.roadmaker.v1.member.service.MemberService;
import com.roadmaker.v1.roadmap.dto.RoadmapDto;
import com.roadmaker.v1.roadmap.service.RoadmapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final RoadmapService roadmapService;
    private final CommentService commentService;
    private final ImageService imageService;

    @LoginRequired
    @PostMapping("/profile/avatar")
    public ResponseEntity<MemberAvatarUpdateResponse> uploadMemberAvatar(
            @RequestPart(value = "file") MultipartFile image,
            @LoginMember Member member
    ) throws IOException {
        String avatarUrl = imageService.uploadImage(image);
        MemberAvatarUpdateResponse response = memberService.updateAvatarUrl(member, avatarUrl);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberFindResponse> findMember(@PathVariable Long memberId) {
        MemberFindResponse memberResponse = memberService.findMemberByMemberId(memberId);
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @GetMapping("/{memberId}/comments")
    public ResponseEntity<CommentResponse> findMemberComments(@PathVariable Long memberId, @RequestParam(name = "page") Integer page) {
        final int size = 8;
        CommentResponse commentsInPage = commentService.findByMemberIdAndPageRequest(memberId, page, size);
        return new ResponseEntity<>(commentsInPage, HttpStatus.OK);
    }

    @LoginRequired
    @PatchMapping("/profile")
    public ResponseEntity<Object> updateProfile(
            @Valid @RequestBody MemberUpdateRequest request,
            @LoginMember Member member
    ) {
        memberService.updateProfile(request, member);
        return ResponseEntity.ok().build();
    }

    @LoginRequired
    @GetMapping("/{nickname}/in-progress-roadmaps")
    public ResponseEntity<List<RoadmapDto>> callRoadmapJoined(@PathVariable String nickname) {
        Long memberId = memberService.findMemberByNickname(nickname).getId();
        //해당 멤버를 찾을 수 없는경우: 발생할 일 없음

        //이 멤버가 조인하고 잇는 모든 로드맵을 dto형태로의 리스트로 전달
        List<RoadmapDto> joiningRoadmaps = roadmapService.findRoadmapJoinedByMemberId(memberId);
        return new ResponseEntity<>(joiningRoadmaps, HttpStatus.OK);
    }

    @LoginRequired
    @GetMapping("/{nickname}/roadmaps")
    public ResponseEntity<List<RoadmapDto>> callRoadmapCreated(@PathVariable String nickname) {
        Long memberId = memberService.findMemberByNickname(nickname).getId();

        //이 멤버가 만든 모든 로드맵을 dto형태로의 리스트로 전달
        List<RoadmapDto> createdRoadmaps = roadmapService.findRoadmapCreatedByMemberId(memberId);
        return new ResponseEntity<>(createdRoadmaps, HttpStatus.OK);
    }

}
