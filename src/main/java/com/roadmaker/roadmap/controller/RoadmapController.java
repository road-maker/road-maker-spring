package com.roadmaker.roadmap.controller;

import com.roadmaker.member.domain.entity.Member;
import com.roadmaker.member.domain.repository.MemberRepository;
import com.roadmaker.roadmap.domain.entity.*;
import com.roadmaker.roadmap.domain.repository.InProgressRoadmapRepository;
import com.roadmaker.roadmap.domain.repository.RoadmapEdgeRepository;
import com.roadmaker.roadmap.domain.repository.RoadmapNodeRepository;
import com.roadmaker.roadmap.domain.repository.RoadmapRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roadmaps")
public class RoadmapController {
    private final RoadmapRepository roadmapRepository;
    private final RoadmapEdgeRepository roadmapEdgeRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final MemberRepository memberRepository;
    private final InProgressRoadmapRepository inProgressRoadmapRepository;

    @PostMapping("/dummy")
    public void createDummyRoadmap() {
        //Roadmap roadmap = new Roadmap("백엔드 개발자 로드맵", "어느 한 천재가 만든 백엔드 개발자 로드맵입니다.");
        //roadmapRepository.save(roadmap);
        // 노드랑 엣지 만들기
    }

    //리턴 방법도 프론트와 협의
    @GetMapping(path = "/api/load-roadmap/{roadmapId}")
    public Map<String, Object> loadRoadmap(@PathVariable Long roadmapId) {

        Optional<Roadmap> roadmap = roadmapRepository.findById(roadmapId);
        List<RoadmapNode> roadmapNodes = roadmapNodeRepository.findByRoadmapId(roadmapId); //없다면 빈 리스트
        List<RoadmapEdge> roadmapEdges = roadmapEdgeRepository.findByRoadmapId(roadmapId);

        Map<String, Object> result = new HashMap<>();
        result.put("roadmap", roadmap.orElse(null));
        result.put("nodes", roadmapNodes);
        result.put("edges", roadmapEdges);

        System.out.println(result); // 확인

        for (Map.Entry<String, Object> eachResult: result.entrySet()) {
            Object value = eachResult.getValue();
            if (value == null) {
                return null; //오류 처리 어떻게?  -> 프론트와 협의
            }
        }
        return result;
    }

    @PostMapping(path="/api/roadmaps/{roadmapId}/join")
    public void joinRoadmap(HttpServletResponse response, @PathVariable Long roadmapId) {
        //초기화가 필요한 것들-> id(자동 생성), roadmap: 로드맵 id주소로 전달, member: jwt추출

        String memberId = "awhidte@gmail.com"; //

        Optional<Roadmap> roadmapOptional = roadmapRepository.findById(roadmapId);
        List<RoadmapNode> roadmapNodes = roadmapNodeRepository.findByRoadmapId(roadmapId);
        Optional<Member> memberOptional = memberRepository.findByEmail(memberId);


        Roadmap roadmap = roadmapOptional.orElse(null);
        Member member = memberOptional.orElse(null);
        if (roadmap == null || member == null) {
            System.out.println("Roadmap or Member not found");
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

        System.out.println("Roadmap Joined");

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @PatchMapping("/api/inProgressNodes/{inProgressNodeId}/done")
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
            System.out.println("cant find specified node");
        }
    }

}
