package com.roadmaker.roadmap.controller;

import com.roadmaker.roadmap.domain.entity.RoadmapNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roadmaker.roadmap.domain.entity.Roadmap;
import com.roadmaker.roadmap.domain.entity.RoadmapEdge;
import com.roadmaker.roadmap.domain.repository.RoadmapEdgeRepository;
import com.roadmaker.roadmap.domain.repository.RoadmapNodeRepository;
import com.roadmaker.roadmap.domain.repository.RoadmapRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roadmaps")
public class RoadmapController {
    private final RoadmapRepository roadmapRepository;
    private final RoadmapNodeRepository roadmapNodeRepository;
    private final RoadmapEdgeRepository roadmapEdgeRepository;

    @PostMapping("/dummy")
    public void createDummyRoadmap() {
//        Roadmap roadmap = new Roadmap("백엔드 개발자 로드맵", "어느 한 천재가 만든 백엔드 개발자 로드맵입니다.");
        Roadmap roadmap = Roadmap.builder()
                .title("백엔드 개발자 로드맵")
                .description("어느 한 천재가 만든 백엔드 개발자 로드맵입니다.")
                .build();
        roadmapRepository.save(roadmap);

        // 노드랑 엣지 만들기
        RoadmapNode node1 = RoadmapNode.builder()
                .roadmap(roadmap)
                .clientNodeId("1")
                .type("input")
                .label("Java 기초 다지기")
                .xPosition(250)
                .yPosition(0)
                .detailedContent("<p>Java is general-purpose language, primarily used for Internet-based applications. It was created in 1995 by James Gosling at Sun Microsystems and is one of the most popular options for backend developers.</p>")
                .build();

        RoadmapNode node2 = RoadmapNode.builder()
                .roadmap(roadmap)
                .clientNodeId("1")
                .type("input")
                .label("스프링 기초 다지기")
                .xPosition(260)
                .yPosition(100)
                .detailedContent("<p>Spring is an open-source framework that provides a comprehensive programming and configuration model for modern Java-based enterprise applications. The core module of Spring, also known as the “Spring Core” module, is at the heart of the framework and provides the fundamental functionality for dependency injection (DI) and inversion of control (IoC). In addition to dependency injection, the Spring Core module also provides several other features, such as:</p>")
                .build();

        RoadmapNode node3 = RoadmapNode.builder()
                .roadmap(roadmap)
                .clientNodeId("1")
                .type("input")
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
}
