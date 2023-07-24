package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmapnodepositionabsolute.RoadmapNodePositionAbsolute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoadmapNodePositionAbsoluteDto {
    private Integer x;
    private Integer y;

    public RoadmapNodePositionAbsolute toEntity() {
        return RoadmapNodePositionAbsolute.builder()
                .x(this.x)
                .y(this.y)
                .build();
    }
}