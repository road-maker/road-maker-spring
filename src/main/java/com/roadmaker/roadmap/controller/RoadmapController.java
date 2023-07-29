package com.roadmaker.roadmap.controller;

import com.roadmaker.comment.dto.CommentDto;
import com.roadmaker.comment.service.CommentService;
import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.roadmap.dto.*;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.entity.roadmapeditor.RoadmapEditor;
import com.roadmaker.roadmap.entity.roadmapeditor.RoadmapEditorRepository;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.service.RoadmapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RoadmapEditorRepository roadmapEditorRepository;
    private final CommentService commentService;

    // 로드맵 발행
    @LoginRequired
    @PostMapping
    public ResponseEntity<Long> createRoadmap(@Valid @RequestBody CreateRoadmapRequest createRoadmapRequest, @LoginMember Member member) {
        Long roadmapId = roadmapService.createRoadmap(createRoadmapRequest, member);

        return new ResponseEntity<>(roadmapId, HttpStatus.CREATED);
    }

//    /api/roadmaps?
    @GetMapping
    public ResponseEntity<List<RoadmapDto>> getRoadmaps() {
        List<Roadmap> roadmaps = roadmapRepository.findAll();
        List<RoadmapDto> roadmapDtos = new ArrayList<>();
        roadmaps.stream().forEach(roadmap -> { //로드맵 정보에 로드맵 작성자와 프로필을 추가하는 루틴
            RoadmapEditor roadmapEditor = roadmapEditorRepository.findByRoadmapIdAndIsOwner(roadmap.getId(), true);
            if(roadmapEditor == null) { //테이블의 구체성이 부족할 때 만들어진 데이터 때문에 nullpointer exception이 발생할 수 있어서 넣어둔 코드, 실제에선 발생이 없어야 함
                roadmapDtos.add(RoadmapDto.of(roadmap));
            } else {
                String ownerNickname = roadmapEditor.getMember().getNickname();
                String ownerProfileUrl = roadmapEditor.getMember().getAvatarUrl();
                roadmapDtos.add(RoadmapDto.of(roadmap, ownerNickname, ownerProfileUrl));
            }
        });
        return new ResponseEntity<>(roadmapDtos, HttpStatus.OK);
    }

    @GetMapping(path = "/load-roadmap/{roadmapId}")
    public ResponseEntity<RoadmapResponse> loadRoadmap(@PathVariable Long roadmapId) {

        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);
        if (roadmap == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<CommentDto> commentDtos = commentService.callRoadmapComment(roadmapId);

        RoadmapResponse roadmapResponse = RoadmapResponse.of(roadmap, commentDtos);

        return new ResponseEntity<>(roadmapResponse, HttpStatus.OK);
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
}
