package com.roadmaker.roadmap.controller;

import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
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

import java.security.Security;
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
    private final MemberService memberService;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final InProgressNodeRepository inProgressNodeRepository;
    private final RoadmapEditorRepository roadmapEditorRepository;
    private final RoadmapViewportRepository roadmapViewportRepository;

    // 로드맵 발행
    @PostMapping
    public ResponseEntity<Long> createRoadmap(@RequestBody RoadmapRequest roadmapRequest) {
        Member member = memberService.getLoggedInMember();

        Long roadmapId = roadmapService.createRoadmap(roadmapRequest, member);

        return new ResponseEntity<>(roadmapId, HttpStatus.CREATED);
    }

    @GetMapping(path = "/load-roadmap/{roadmapId}")

    public ResponseEntity<RoadmapResponse> loadRoadmap(@PathVariable Long roadmapId) {

        Optional<Roadmap> roadmap = roadmapRepository.findById(roadmapId);

        if (roadmap.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RoadmapResponse roadmapResponse = RoadmapResponse.of(roadmap.get());

        return new ResponseEntity<RoadmapResponse>(roadmapResponse, HttpStatus.OK);
    }

    @PostMapping(path="/{roadmapId}/join")
    public void joinRoadmap(HttpServletResponse response, @PathVariable Long roadmapId) {
        //초기화가 필요한 것들-> id(자동 생성), roadmap: 로드맵 id주소로 전달, member: jwt추출

        Optional<Roadmap> roadmapOptional = roadmapRepository.findById(roadmapId);
        List<RoadmapNode> roadmapNodes = roadmapNodeRepository.findByRoadmapId(roadmapId);
        Member member = memberService.getLoggedInMember();

        Roadmap roadmap = roadmapOptional.orElse(null);
        if (roadmap == null) {
            log.info("Roadmap not found");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        //해당 유저가 이미 join하고 있다면 오류 발생
        if (inProgressRoadmapRepository.findByRoadmapIdAndMemberId(roadmapId, member.getId()).isPresent()) {
            log.info("해당 유저는 이미 이 roadmap 을 join 하고 있음");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
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
                    inProgressNodeRepository.save(inProgressNode);
                    inProgressNodes.add(inProgressNode);
                });

        InProgressRoadmap inProgressRoadmap= InProgressRoadmap.builder()
                .roadmap(roadmap)
                .member(member)
                .inProgressNodes(inProgressNodes)
                .done(false)
                .build();
        inProgressRoadmapRepository.save(inProgressRoadmap);

//        List<String> roadmapDetail = new ArrayList<>();
//        List<String> roadmapNodesDetail = new ArrayList<>();
//        roadmapDetail.add(inProgressRoadmap.toString());
//        roadmapNodesDetail.add(inProgressNodes.toString());
//        List<String> all = new ArrayList<>();
//        all.add(roadmapDetail.toString());
//        all.add(roadmapNodesDetail.toString());

        response.setStatus(HttpServletResponse.SC_CREATED);
//        System.out.println(all);
    }

    @PatchMapping("/in-progress-nodes/{inProgressNodeId}/done")
    public void nodeDone (@PathVariable Long inProgressNodeId, HttpServletResponse response) {
        Optional<InProgressNode> inProgressNodeOptional = inProgressNodeRepository.findById(inProgressNodeId);
        InProgressNode inProgressNode = inProgressNodeOptional.orElse(null);
        String memberEmail = SecurityUtil.getLoggedInMemberEmail();
        Member member = inProgressNode.getMember();

        if(member == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (inProgressNode.getMember().getEmail().equals(memberEmail)) {
            if (Boolean.TRUE.equals(inProgressNode.getDone())) {
                inProgressNode.setDone(false);
                inProgressNodeRepository.save(inProgressNode);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                inProgressNode.setDone(true);
                inProgressNodeRepository.save(inProgressNode);
                response.setStatus(HttpServletResponse.SC_OK);
            }

//            List<String> node = new ArrayList<>();
//            node.add("Node Id: " + inProgressNode.getId());
//            node.add("UserEmail: " + inProgressNode.getMember().getEmail());
//            node.add("Roadmap: " + inProgressNode.getRoadmap().getTitle());
//            node.add("Done: " + inProgressNode.getDone());
//            System.out.println(node);
        } else {
            log.info("not a progressing member");
        }
    }
}
