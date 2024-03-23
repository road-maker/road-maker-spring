package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.roadmap.entity.roadmapnodepositionabsolute.RoadmapNodePositionAbsolute;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapNodePositionAbsoluteDto {
    private Integer x;
    private Integer y;

    public RoadmapNodePositionAbsolute toEntity() {
        return RoadmapNodePositionAbsolute.builder()
                .x(this.x)
                .y(this.y)
                .build();
    }

    public static RoadmapNodePositionAbsoluteDto of(RoadmapNodePositionAbsolute roadmapNodePositionAbsolute) {
        return RoadmapNodePositionAbsoluteDto.builder()
                .x(roadmapNodePositionAbsolute.getX())
                .y(roadmapNodePositionAbsolute.getY())
                .build();
    }
}