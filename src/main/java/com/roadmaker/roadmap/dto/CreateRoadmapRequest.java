package com.roadmaker.roadmap.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CreateRoadmapRequest {
    private RoadmapDto roadmap;
    private RoadmapViewportDto viewport;
    private List<RoadmapEdgeDto> roadmapEdges;
    private List<RoadmapNodeDto> roadmapNodes;
}

