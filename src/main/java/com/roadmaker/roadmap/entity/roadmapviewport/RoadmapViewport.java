package com.roadmaker.roadmap.entity.roadmapviewport;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
public class RoadmapViewport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
