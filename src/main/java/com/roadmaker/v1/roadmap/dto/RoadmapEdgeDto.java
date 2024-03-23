package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.v1.roadmap.entity.roadmapedge.RoadmapEdge;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapEdgeDto {
    private String id;
    private String source;
    private String target;
    private String type;
    private Boolean animated;

    public RoadmapEdge toEntity(Roadmap roadmap) {
        return RoadmapEdge.builder()
                .clientEdgeId(this.id)
                .roadmap(roadmap)
                .source(this.source)
                .target(this.target)
                .type(this.type)
                .animated(this.animated)
                .build();
    }

    public static RoadmapEdgeDto of(RoadmapEdge roadmapEdge) {
        return RoadmapEdgeDto.builder()
                .id(roadmapEdge.getClientEdgeId())
                .source(roadmapEdge.getSource())
                .target(roadmapEdge.getTarget())
                .type(roadmapEdge.getType())
                .animated(roadmapEdge.getAnimated())
                .build();
    }
}
