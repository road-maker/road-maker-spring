package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmapnodeposition.RoadmapNodePosition;
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
}