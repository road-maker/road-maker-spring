package com.roadmaker.roadmap.entity.roadmapnodestyle;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class RoadmapNodeStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String background;

    private String border;

    private Integer borderRadius;

    private String fontSize;
}
