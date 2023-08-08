package com.roadmaker.roadmap.controller;

import com.roadmaker.comment.dto.CommentResponse;
import com.roadmaker.comment.service.CommentService;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.image.dto.UploadImageResponse;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmap;
import com.roadmaker.inprogressroadmap.entity.InProgressRoadmapRepository;
import com.roadmaker.like.service.LikeService;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeyword;
import com.roadmaker.roadmap.entity.blogkeyword.BlogKeywordRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.service.RoadmapService;
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

@RestController @Slf4j @Validated
@RequiredArgsConstructor
@RequestMapping("/api/roadmaps")
public class RoadmapController {

    private final MemberService memberService;
    private final RoadmapService roadmapService;
    private final InProgressNodeRepository inProgressNodeRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final CommentService commentService;
    private final LikeService likeService;

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
            @RequestPart(value = "file") MultipartFile multipartFile,
            @LoginMember Member member) throws IOException {

        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);

        if (!roadmap.getMember().getId().equals(member.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UploadImageResponse uploadImageResponse = roadmapService.uploadThumbnail(roadmap, multipartFile);

        return new ResponseEntity<>(uploadImageResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<RoadmapFindResponse> getRoadmaps(@RequestParam(name = "page") Integer page,@RequestParam(name="order-type") String orderType) {
        int size = 8; //default로 페이지에서 불러올 요소의 갯수

        RoadmapFindResponse roadmaps = roadmapService.findByPage(page, size, orderType); // 뭘로 구분할 것인지 정리 필요

        return new ResponseEntity<>(roadmaps, HttpStatus.OK);
    }

    @GetMapping("/{roadmapId}")
    public ResponseEntity<RoadmapResponse> getRoadmap(@PathVariable Long roadmapId) {
        Optional<Member> memberOpt = memberService.getLoggedInMember();
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

//    @LoginRequired
//    @GetMapping("/{roadmapId}/auth")
//    public ResponseEntity<RoadmapResponse> loadRoadmapWithAuth(@PathVariable Long roadmapId, @LoginMember Member member) {
//        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);
//        RoadmapResponse roadmapResponse;
//        boolean isLiked = likeService.isLiked(roadmapId, member.getId());
//
//        Optional<InProgressRoadmap> inProgressRoadmap = inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmapId, member.getId());
//
//        if (inProgressRoadmap.isEmpty()) {
//            roadmapResponse = RoadmapResponse.of(roadmap, isLiked);
//        } else {
//            List<InProgressNode> inProgressNodes = inProgressNodeRepository.findByRoadmapIdAndMemberId(roadmapId, member.getId());
//            roadmapResponse = RoadmapResponse.of(roadmap, isLiked, inProgressNodes);
//        }
//
//        return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
//    }

    @GetMapping(path="/load-roadmap/{roadmapId}/comments")
    public ResponseEntity<CommentResponse> loadRoadmapComments(@PathVariable Long roadmapId, @RequestParam Integer page) {
        int size = 8;
        return new ResponseEntity<> (commentService.findCommentByRoadmapIdAndPageRequest(roadmapId, page, size), HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping(path="/{roadmapId}/join")
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
    public ResponseEntity<RoadmapFindResponse> searchTitleByKeyword(@PathVariable String keyword, @RequestParam(value = "page") Integer page) {
        int size = 8;
        return new ResponseEntity<> (roadmapService.findRoadmapByKeyword(keyword, size, page-1), HttpStatus.OK);
    }

    @PostMapping("/blog_keyword")
    public ResponseEntity<HttpStatus> setKeyword(@RequestBody BlogKeywordRequest request){
        boolean result = roadmapService.setBlogKeyword(request);

        if (!result) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/boj_problem")
    public ResponseEntity<HttpStatus> setProblem(@RequestBody BojProbRequest request){
        boolean result = roadmapService.setBojProblem(request);

        if (!result) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
