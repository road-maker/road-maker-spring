package com.roadmaker.roadmap.entity.roadmapnodepositionabsolute;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class RoadmapNodePositionAbsolute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer x;
    private Integer y;
}
