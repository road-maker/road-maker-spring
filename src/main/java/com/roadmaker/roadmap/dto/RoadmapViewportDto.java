package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
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

    public static RoadmapViewportDto of(RoadmapViewport roadmapViewport) {
        return RoadmapViewportDto.builder()
                .x(roadmapViewport.getX())
                .y(roadmapViewport.getY())
                .zoom(roadmapViewport.getZoom())
                .build();
    }
}