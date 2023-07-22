package com.roadmaker.roadmap.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROADMAP_NODE")
public class RoadmapNode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    private Roadmap roadmap;

    private String clientNodeId;

    private String type;

    private Integer xPosition;

    private Integer yPosition;

    private String label;

    private String background;

    private String border;

    private Integer borderRadius;

    private Integer fontSize;

    @Column(columnDefinition = "TEXT")
    private String detailedContent;

    @Builder
    public RoadmapNode(Roadmap roadmap, String clientNodeId, String type, Integer xPosition, Integer yPosition, String label, String background, String border, Integer borderRadius, Integer fontSize, String detailedContent) {
        this.roadmap = roadmap;
        this.clientNodeId = clientNodeId;
        this.type = type;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.label = label;
        this.background = background;
        this.border = border;
        this.borderRadius = borderRadius;
        this.fontSize = fontSize;
        this.detailedContent = detailedContent;
    }
}
