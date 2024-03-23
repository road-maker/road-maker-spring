package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.roadmap.entity.roadmapnodeposition.RoadmapNodePosition;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapNodePositionDto {
    private Integer x;
    private Integer y;

    public RoadmapNodePosition toEntity() {
        return RoadmapNodePosition.builder()
                .x(this.x)
                .y(this.y)
                .build();
    }

    public static RoadmapNodePositionDto of(RoadmapNodePosition roadmapNodePosition) {
        return RoadmapNodePositionDto.builder()
                .x(roadmapNodePosition.getX())
                .y(roadmapNodePosition.getY())
                .build();
    }
}