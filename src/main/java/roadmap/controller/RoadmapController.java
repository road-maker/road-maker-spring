package roadmap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roadmap.domain.entity.Roadmap;
import roadmap.domain.repository.RoadmapRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roadmaps")
public class RoadmapController {
    private final RoadmapRepository roadmapRepository;
    private final Roadmap roadmap;

    @PostMapping("/dummy")
    public void createDummyRoadmap() {
        Roadmap roadmap = new Roadmap("백엔드 개발자 로드맵", "어느 한 천재가 만든 백엔드 개발자 로드맵입니다.");
        roadmapRepository.save(roadmap);
        // 노드랑 엣지 만들기
    }
}
