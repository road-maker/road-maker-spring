package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapResponse {
    private RoadmapDto roadmap;
    private RoadmapViewportDto viewport;
    private List<RoadmapEdgeDto> edges;
    private List<RoadmapNodeDto> nodes;

    /** 로그인 하지 않거나, 로드맵에 참가하지 않은 사람의 DTO */
    public static RoadmapResponse of(Roadmap roadmap) {
        return RoadmapResponse.builder()
                .roadmap(RoadmapDto.of(roadmap))
                .viewport(RoadmapViewportDto.of(roadmap.getRoadmapViewport()))
                .nodes(roadmap.getRoadmapNodes().stream().map(RoadmapNodeDto::of).toList())
                .edges(roadmap.getRoadmapEdges().stream().map(RoadmapEdgeDto::of).toList())
                .build();
    }

    /** 로드맵에 참가한 사람의 DTO */
    public static RoadmapResponse of(Roadmap roadmap, List<InProgressNode> inProgressNodes) {
        return RoadmapResponse.builder()
                .roadmap(RoadmapDto.of(roadmap))
                .viewport(RoadmapViewportDto.of(roadmap.getRoadmapViewport()))
                .nodes(inProgressNodes.stream().map(RoadmapNodeDto::of).toList())
                .edges(roadmap.getRoadmapEdges().stream().map(RoadmapEdgeDto::of).toList())
                .build();
    }
}
