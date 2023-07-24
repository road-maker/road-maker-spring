package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class RoadmapViewportDto {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal zoom;

    public RoadmapViewport toEntity() {
        return RoadmapViewport.builder()
                .x(this.x)
                .y(this.y)
                .zoom(this.zoom)
                .build();
    }
}