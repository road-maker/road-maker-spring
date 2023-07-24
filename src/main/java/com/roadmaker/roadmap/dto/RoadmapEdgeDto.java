package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoadmapEdgeDto {
    private String id;
    private String source;
    private String target;
    private String type;
    private Boolean isAnimated;

    public RoadmapEdge toEntity(Roadmap roadmap) {
        return RoadmapEdge.builder()
                .clientEdgeId(this.id)
                .roadmap(roadmap)
                .source(this.source)
                .target(this.target)
                .type(this.type)
                .animated(this.isAnimated)
                .build();
    }
}
