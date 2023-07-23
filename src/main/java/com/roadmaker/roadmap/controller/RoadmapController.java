package com.roadmaker.roadmap.controller;

import com.roadmaker.member.authentication.SecurityUtil;
import com.roadmaker.member.service.MemberService;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNodeRepository;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
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
    private final RoadmapRepository roadmapRepository;
    private final RoadmapEdgeRepository roadmapEdgeRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final MemberService memberService;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;
    private final InProgressNodeRepository inProgressNodeRepository;

    @PostMapping("/dummy")
    public void createDummyRoadmap() {
        Roadmap roadmap = Roadmap.builder()
                .title("백엔드 개발자 로드맵")
                .description("어느 한 천재가 만든 백엔드 개발자 로드맵입니다.")
                .build();
        roadmapRepository.save(roadmap);

        // 노드랑 엣지 만들기
        RoadmapNode node1 = RoadmapNode.builder()
                .roadmap(roadmap)
                .clientNodeId("1")
                .label("Java 기초 다지기")
                .xPosition(250)
                .yPosition(0)
                .detailedContent("<p>Java is general-purpose language, primarily used for Internet-based applications. It was created in 1995 by James Gosling at Sun Microsystems and is one of the most popular options for backend developers.</p>")
                .build();

        RoadmapNode node2 = RoadmapNode.builder()
                .roadmap(roadmap)
                .clientNodeId("1")
                .label("스프링 기초 다지기")
                .xPosition(260)
                .yPosition(100)
                .detailedContent("<p>Spring is an open-source framework that provides a comprehensive programming and configuration model for modern Java-based enterprise applications. The core module of Spring, also known as the “Spring Core” module, is at the heart of the framework and provides the fundamental functionality for dependency injection (DI) and inversion of control (IoC). In addition to dependency injection, the Spring Core module also provides several other features, such as:</p>")
                .build();

        RoadmapNode node3 = RoadmapNode.builder()
                .roadmap(roadmap)
                .clientNodeId("1")
                .label("크래프톤 정글 수료하기")
                .xPosition(240)
                .yPosition(210)
                .detailedContent("<p>더이상의 자세한 설명은 생략한다.</p>")
                .build();

        RoadmapEdge edge1 = RoadmapEdge.builder()
                .roadmap(roadmap)
                .clientEdgeId("e1-2")
                .source("1")
                .target("2")
                .build();

        RoadmapEdge edge2 = RoadmapEdge.builder()
                .roadmap(roadmap)
                .clientEdgeId("e1-3")
                .source("2")
                .target("3")
                .build();

        // 로드맵을 쿼리했을 때 원래 값으로 돌려줘야 한다.
        // 로드맵 제목이랑 이런 것을 동적으로 바꿀 수 있게 하자

        roadmapNodeRepository.save(node1);
        roadmapNodeRepository.save(node2);
        roadmapNodeRepository.save(node3);
        roadmapEdgeRepository.save(edge1);
        roadmapEdgeRepository.save(edge2);
    }

    @GetMapping(path = "/load-roadmap/{roadmapId}")
    public Map<String, Object> loadRoadmap(HttpServletResponse response, @PathVariable Long roadmapId) {

        Optional<Roadmap> roadmap = roadmapRepository.findById(roadmapId);
        List<RoadmapNode> roadmapNodes = roadmapNodeRepository.findByRoadmapId(roadmapId); //없다면 빈 리스트
        List<RoadmapEdge> roadmapEdges = roadmapEdgeRepository.findByRoadmapId(roadmapId);

        Map<String, Object> result = new HashMap<>();
        result.put("roadmap", roadmap.orElse(null));
//        result.put("nodes", roadmapNodes);
//        result.put("edges", roadmapEdges);

        for (Map.Entry<String, Object> eachResult: result.entrySet()) {
            Object value = eachResult.getValue();
            if (value == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null; //오류 처리 어떻게?  -> 프론트와 협의
            }
        }
        return result;
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
