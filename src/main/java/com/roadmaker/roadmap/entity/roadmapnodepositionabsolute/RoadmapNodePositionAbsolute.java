package com.roadmaker.roadmap.entity.roadmapnodepositionabsolute;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadmapNodePositionAbsolute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer x;
    private Integer y;

    @Builder
    public RoadmapNodePositionAbsolute(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}
