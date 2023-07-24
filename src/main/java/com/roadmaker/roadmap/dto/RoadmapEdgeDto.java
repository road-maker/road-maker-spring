package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
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
}
