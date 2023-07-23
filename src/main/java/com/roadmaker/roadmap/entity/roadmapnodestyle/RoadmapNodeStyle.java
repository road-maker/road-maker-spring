package com.roadmaker.roadmap.entity.roadmapnodestyle;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;


@Entity
public class RoadmapNodeStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String background;
    private String border;
    private Integer borderRadius;
    private Integer fontSize;

    @Builder
    public RoadmapNodeStyle(String background, String border, Integer borderRadius, Integer fontSize) {
        this.background = background;
        this.border = border;
        this.borderRadius = borderRadius;
        this.fontSize = fontSize;
    }
}
