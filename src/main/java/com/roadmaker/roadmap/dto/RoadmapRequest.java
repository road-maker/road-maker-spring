package com.roadmaker.roadmap.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapRequest {
    private RoadmapDto roadmap;
    private RoadmapViewportDto viewport;
       private List<RoadmapEdgeDto> edges;
    private List<RoadmapNodeDto> nodes;
}

