package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapResponse {
    private RoadmapDto roadmap;
    private RoadmapViewportDto viewport;
    private List<RoadmapEdgeDto> roadmapEdges;
    private List<RoadmapNodeDto> roadmapNodes;
    private List<CommentDto> commentDtos;

    public static RoadmapResponse of(Roadmap roadmap) {
        return RoadmapResponse.builder()
                .roadmap(RoadmapDto.of(roadmap))
                .viewport(RoadmapViewportDto.of(roadmap.getRoadmapViewport()))
                .roadmapNodes(roadmap.getRoadmapNodes().stream().map(roadmapNode -> RoadmapNodeDto.of(roadmapNode)).collect(Collectors.toList()))
                .roadmapEdges(roadmap.getRoadmapEdges().stream().map(roadmapEdge -> RoadmapEdgeDto.of(roadmapEdge)).collect(Collectors.toList()))
                .build();
    }

}
