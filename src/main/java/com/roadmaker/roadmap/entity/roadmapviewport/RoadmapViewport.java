package com.roadmaker.roadmap.entity.roadmapviewport;

import jakarta.persistence.Entity;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
public class RoadmapViewport {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal zoom;

    @Builder
    public RoadmapViewport(BigDecimal x, BigDecimal y, BigDecimal zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }
}
