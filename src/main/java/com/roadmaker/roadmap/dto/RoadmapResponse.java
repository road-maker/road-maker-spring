package com.roadmaker.roadmap.dto;

import com.roadmaker.comment.dto.CommentDto;
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

    public static RoadmapResponse of(Roadmap roadmap) {
        return RoadmapResponse.builder()
                .roadmap(RoadmapDto.of(roadmap))
                .viewport(RoadmapViewportDto.of(roadmap.getRoadmapViewport()))
                .nodes(roadmap.getRoadmapNodes().stream().map(RoadmapNodeDto::of).toList())
                .edges(roadmap.getRoadmapEdges().stream().map(RoadmapEdgeDto::of).toList())
                .build();
    }
}
