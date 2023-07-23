package com.roadmaker.roadmap.entity.roadmapnodestyle;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class RoadmapNodeStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String background;
    private String border;
    private Integer borderRadius;
    private String fontSize;
}
