package com.roadmaker.roadmap.controller;

import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.dto.RoadmapDto;
import com.roadmaker.roadmap.dto.RoadmapRequest;
import com.roadmaker.roadmap.dto.RoadmapResponse;
import com.roadmaker.roadmap.entity.roadmapeditor.RoadmapEditorRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmap;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewportRepository;
import com.roadmaker.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdgeRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNodeRepository;
import com.roadmaker.roadmap.entity.roadmap.RoadmapRepository;
import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmapRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController @Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/roadmaps")
public class RoadmapController {
    private final MemberService memberService;
    private final RoadmapService roadmapService;

    private final RoadmapRepository roadmapRepository;
    private final RoadmapEdgeRepository roadmapEdgeRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final MemberRepository memberRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final RoadmapEditorRepository roadmapEditorRepository;
    private final RoadmapViewportRepository roadmapViewportRepository;

    // 로드맵 발행
    @LoginRequired
    @PostMapping
    public ResponseEntity<Long> createRoadmap(@RequestBody RoadmapRequest roadmapRequest, @LoginMember Member member) {
        Long roadmapId = roadmapService.createRoadmap(roadmapRequest, member);

        return new ResponseEntity<>(roadmapId, HttpStatus.CREATED);
    }

    //리턴 방법도 프론트와 협의
    @GetMapping(path = "/load-roadmap/{roadmapId}")
    public ResponseEntity<RoadmapResponse> loadRoadmap(@PathVariable Long roadmapId) {

        Optional<Roadmap> roadmap = roadmapRepository.findById(roadmapId);

        if (roadmap.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RoadmapResponse roadmapResponse = RoadmapResponse.of(roadmap.get());

        return new ResponseEntity<RoadmapResponse>(roadmapResponse, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping(path="/{roadmapId}/join")
    public void joinRoadmap(HttpServletResponse response, @PathVariable Long roadmapId, @LoginMember Member member) {
        //초기화가 필요한 것들-> id(자동 생성), roadmap: 로드맵 id주소로 전달, member: jwt추출


        Optional<Roadmap> roadmapOptional = roadmapRepository.findById(roadmapId);
        List<RoadmapNode> roadmapNodes = roadmapNodeRepository.findByRoadmapId(roadmapId);

        Roadmap roadmap = roadmapOptional.orElse(null);
        if (roadmap == null || member == null) {
            log.info("Roadmap or Member not found");
            return;
        }

        List<InProgressNode> inProgressNodes = new ArrayList<>();
        roadmapNodes.stream()
                .forEach(node -> {InProgressNode inProgressNode = InProgressNode.builder()
                        .roadmap(roadmap)
                        .roadmapNode(node)
                        .member(member)
                        .done(false)
                        .build();
                    inProgressNodes.add(inProgressNode);
                });

        InProgressRoadmap.builder()
                .roadmap(roadmap)
                .member(member)
                .inProgressNodes(inProgressNodes)
                .done(false)
                .build();

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @LoginRequired
    @PatchMapping("/inProgressNodes/{inProgressNodeId}/done")
    public void nodeDone (@PathVariable Long inProgressNodeId, HttpServletResponse response) {
        // inProgressNode테이블에서 정보를 가져와 업데이트
        // 유저 정보도 가져와야 할 것 같다. jwt를 사용. -> 프론트와 협의
        //1. 프론트에서 유저정보 거를 수 없을 때
//        Long memberId = 1999999L;
//        Optional<InProgressRoadmap> inProgressNodeOptional = inProgressRoadmapRepository.findByIdAndMemberId(inProgressNodeId, memberId);
        //2. 프론트에서 유저정보 걸러줄 때
        Optional<InProgressRoadmap> inProgressRoadmapOptional = inProgressRoadmapRepository.findById(inProgressNodeId);
        InProgressRoadmap inProgressRoadmap = inProgressRoadmapOptional.orElse(null);
        if (inProgressRoadmap != null) {
            inProgressRoadmap.setDone(true);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            log.info("Can not find specified node");
        }
    }
}
