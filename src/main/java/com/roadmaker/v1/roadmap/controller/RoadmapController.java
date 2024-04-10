package com.roadmaker.v1.roadmap.controller;

import com.roadmaker.v1.auth.service.AuthService;
import com.roadmaker.v1.comment.dto.response.CommentResponse;
import com.roadmaker.v1.comment.service.CommentService;
import com.roadmaker.global.annotation.LoginMember;
import com.roadmaker.global.annotation.LoginRequired;
import com.roadmaker.v1.image.dto.UploadImageResponse;
import com.roadmaker.v1.image.service.ImageService;
import com.roadmaker.v1.inprogressroadmap.entity.InProgressRoadmap;
import com.roadmaker.v1.inprogressroadmap.entity.InProgressRoadmapRepository;
import com.roadmaker.v1.like.service.LikeService;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.roadmap.dto.*;
import com.roadmaker.v1.roadmap.dto.response.RoadmapCommentPagingResponse;
import com.roadmaker.v1.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.v1.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.v1.roadmap.service.RoadmapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/roadmaps")
public class RoadmapController {

    private final AuthService authService;
    private final RoadmapService roadmapService;
    private final ImageService imageService;
    private final CommentService commentService;
    private final LikeService likeService;

    private final InProgressNodeRepository inProgressNodeRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;

    // 로드맵 발행
    @LoginRequired
    @PostMapping
    public ResponseEntity<Long> createRoadmap(@Valid @RequestBody CreateRoadmapRequest createRoadmapRequest, @LoginMember Member member) {
        Long roadmapId = roadmapService.createRoadmap(createRoadmapRequest, member);

        return new ResponseEntity<>(roadmapId, HttpStatus.CREATED);
    }

    @LoginRequired
    @PostMapping("/{roadmapId}/thumbnails")
    public ResponseEntity<UploadImageResponse> uploadThumbnail(
            @PathVariable Long roadmapId,
            @RequestPart(value = "file") MultipartFile image,
            @LoginMember Member member
    ) throws IOException {
        String thumbnailUrl = imageService.uploadImage(image);

        UploadImageResponse response = roadmapService.uploadThumbnail(roadmapId, member.getId(), thumbnailUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<RoadmapFindResponse> getRoadmaps(@RequestParam(name = "page") Integer page, @RequestParam(name = "order-type") String orderType) {
        int size = 8; //default로 페이지에서 불러올 요소의 갯수

        RoadmapFindResponse roadmaps = roadmapService.findByPage(page, size, orderType); // 뭘로 구분할 것인지 정리 필요

        return new ResponseEntity<>(roadmaps, HttpStatus.OK);
    }

    @GetMapping("/{roadmapId}")
    public ResponseEntity<RoadmapResponse> getRoadmap(@PathVariable Long roadmapId) {
        Optional<Member> memberOpt = authService.getLoggedInMember();
        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);
        Boolean isLiked = false;

        RoadmapResponse roadmapResponse;

        if (memberOpt.isEmpty()) {
            roadmapResponse = RoadmapResponse.of(roadmap, isLiked);
            return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
        }

        Optional<InProgressRoadmap> inProgressRoadmap = inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmapId, memberOpt.get().getId());
        isLiked = likeService.isLiked(roadmapId, memberOpt.get().getId());

        if (inProgressRoadmap.isEmpty()) {
            roadmapResponse = RoadmapResponse.of(roadmap, isLiked);
            return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
        }

        List<InProgressNode> inProgressNodes = inProgressNodeRepository.findByRoadmapIdAndMemberId(roadmapId, memberOpt.get().getId());
        roadmapResponse = RoadmapResponse.of(roadmap, isLiked, inProgressNodes);

        return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/load-roadmap/{roadmapId}")
    public ResponseEntity<RoadmapResponse> loadRoadmap(@PathVariable Long roadmapId) {
        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);

        RoadmapResponse roadmapResponse = RoadmapResponse.of(roadmap, false);

        return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
    }

    @GetMapping("/{roadmapId}/comments")
    public List<RoadmapCommentPagingResponse> findRoadmapComments(
            @PathVariable(name = "roadmapId") Long roadmapId,
            @RequestParam(name = "lastCommentId", defaultValue = "0") Long lastCommentId,
            @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return roadmapService.findRoadmapComments(roadmapId, lastCommentId, size);
    }

    @GetMapping(path = "/load-roadmap/{roadmapId}/comments")
    public ResponseEntity<CommentResponse> loadRoadmapComments(@PathVariable Long roadmapId, @RequestParam Integer page) {
        int size = 8;
        return new ResponseEntity<>(commentService.findCommentByRoadmapIdAndPageRequest(roadmapId, page, size), HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping(path = "/{roadmapId}/join")
    public ResponseEntity<Integer> joinRoadmap(@PathVariable Long roadmapId, @LoginMember Member member) {
        // 1. 필요한 로드맵을 소환: 로드맵 아이디로

        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);

        //2. 로드맵 조인, 실패 시 false 반환: 비즈니스 로직
        Integer count = roadmapService.joinRoadmap(roadmap, member);

        // 3. 성공 신호 전달
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @LoginRequired
    @PatchMapping("/in-progress-nodes/{inProgressNodeId}/done")
    public ResponseEntity<HttpStatus> nodeDone(@PathVariable Long inProgressNodeId, @LoginMember Member member) {
        Optional<InProgressNode> inProgressNodeOptional = inProgressNodeRepository.findById(inProgressNodeId);
        InProgressNode inProgressNode = inProgressNodeOptional.orElse(null);

        // 해당 노드를 찾을 수 없음
        if (inProgressNode == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Long joiningMemberId = Objects.requireNonNull(inProgressNode).getMember().getId();

        //상태 변경 요청을 위한 노드의 주인이 현재 접속한 멤버인지 확인
        if (!joiningMemberId.equals(member.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        NodeStatusChangeDto nodeStatusChangeDto = NodeStatusChangeDto.builder()
                .inProgressNodeId(inProgressNodeId)
                .done(inProgressNode.getDone())
                .build();

        if (roadmapService.changeNodeStatus(nodeStatusChangeDto)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<RoadmapFindResponse> searchTitleByKeyword(@PathVariable String keyword, @RequestParam(value = "page") Integer page) {
        int size = 8;
        return new ResponseEntity<>(roadmapService.findRoadmapByKeyword(keyword, size, page - 1), HttpStatus.OK);
    }
}
