package com.roadmaker.roadmap.entity.roadmapnodedata;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class RoadmapNodeData {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;

    @Builder

    public RoadmapNodeData(String label) {
        this.label = label;
    }
}
