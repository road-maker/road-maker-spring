package com.roadmaker.roadmap.controller;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.comment.service.CommentService;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmap;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmapRepository;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.service.RoadmapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.member.entity.Member;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController @Slf4j @Validated
@RequiredArgsConstructor
@RequestMapping("/api/roadmaps")
public class RoadmapController {
    private final MemberService memberService;
    private final RoadmapService roadmapService;
    private final RoadmapRepository roadmapRepository;
    private final InProgressNodeRepository inProgressNodeRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final CommentService commentService;

    // 로드맵 발행
    @LoginRequired
    @PostMapping
    public ResponseEntity<Long> createRoadmap(@Valid @RequestBody CreateRoadmapRequest createRoadmapRequest, @LoginMember Member member) {
        Long roadmapId = roadmapService.createRoadmap(createRoadmapRequest, member);

        return new ResponseEntity<>(roadmapId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoadmapDto>> getRoadmaps() {
        List<Roadmap> roadmaps = roadmapRepository.findAll();
        List<RoadmapDto> roadmapDtos = roadmaps.stream().map(roadmap -> RoadmapDto.of(roadmap, roadmap.getMember())).toList();

        return new ResponseEntity<>(roadmapDtos, HttpStatus.OK);
    }

    @GetMapping("/{roadmapId}")
    public ResponseEntity<RoadmapResponse> getRoadmap(@PathVariable Long roadmapId) {
        Optional<Member> memberOpt = memberService.getLoggedInMember();
        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);

        RoadmapResponse roadmapResponse;

        if (memberOpt.isEmpty()) {
            roadmapResponse = RoadmapResponse.of(roadmap);
            return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
        }

        Optional<InProgressRoadmap> inProgressRoadmap = inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmapId, memberOpt.get().getId());

        if (inProgressRoadmap.isEmpty()) {
            roadmapResponse = RoadmapResponse.of(roadmap);
            return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
        }

        List<InProgressNode> inProgressNodes = inProgressNodeRepository.findByRoadmapIdAndMemberId(roadmapId, memberOpt.get().getId());
        roadmapResponse = RoadmapResponse.of(roadmap, inProgressNodes);

        return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/load-roadmap/{roadmapId}")
    public ResponseEntity<RoadmapResponse> loadRoadmap(@PathVariable Long roadmapId) {
        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);

        RoadmapResponse roadmapResponse = RoadmapResponse.of(roadmap);

        return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
    }

    @LoginRequired
    @GetMapping("/{roadmapId}/auth")
    public ResponseEntity<RoadmapResponse> loadRoadmapWithAuth(@PathVariable Long roadmapId, @LoginMember Member member) {
        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);
        RoadmapResponse roadmapResponse;

        Optional<InProgressRoadmap> inProgressRoadmap = inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmapId, member.getId());

        if (inProgressRoadmap.isEmpty()) {
            roadmapResponse = RoadmapResponse.of(roadmap);
        } else {
            List<InProgressNode> inProgressNodes = inProgressNodeRepository.findByRoadmapIdAndMemberId(roadmapId, member.getId());
            roadmapResponse = RoadmapResponse.of(roadmap, inProgressNodes);
        }

        return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
    }

    @GetMapping(path="/load-roadmap/{roadmapId}/comments")
    public ResponseEntity<List<CommentDto>> loadRoadmapComments(@PathVariable Long roadmapId, Integer page, Integer size) {
        return new ResponseEntity<> (commentService.findCommentByRoadmapIdAndPageRequest(roadmapId, page, size), HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping(path="/{roadmapId}/join")
    public ResponseEntity<HttpStatus> joinRoadmap(@PathVariable Long roadmapId, @LoginMember Member member) {
        // 1. 필요한 로드맵을 소환: 로드맵 아이디로

        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);

        //2. 로드맵 조인, 실패 시 false 반환: 비즈니스 로직
        roadmapService.joinRoadmap(roadmap, member);

        // 3. 성공 신호 전달
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoginRequired
    @PatchMapping("/in-progress-nodes/{inProgressNodeId}/done")
    public ResponseEntity<HttpStatus> nodeDone (@PathVariable Long inProgressNodeId, @LoginMember Member member) {
        Optional<InProgressNode> inProgressNodeOptional = inProgressNodeRepository.findById(inProgressNodeId);
        InProgressNode inProgressNode = inProgressNodeOptional.orElse(null);

        // 해당 노드를 찾을 수 없음
        if(inProgressNode == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Long joiningMemberId = Objects.requireNonNull(inProgressNode).getMember().getId();

        //상태 변경 요청을 위한 노드의 주인이 현재 접속한 멤버인지 확인
        if(!joiningMemberId.equals(member.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        NodeStatusChangeDto nodeStatusChangeDto = NodeStatusChangeDto.builder()
                        .inProgressNodeId(inProgressNodeId)
                        .done(inProgressNode.getDone())
                        .build();

        if(roadmapService.changeNodeStatus(nodeStatusChangeDto)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<RoadmapDto>> searchTitleByKeyword(@PathVariable String keyword,@RequestParam(value = "size") Integer size, @RequestParam(value = "page") Integer page) {
        return new ResponseEntity<> (roadmapService.findRoadmapByKeyword(keyword, size, page), HttpStatus.OK);
    }

}
