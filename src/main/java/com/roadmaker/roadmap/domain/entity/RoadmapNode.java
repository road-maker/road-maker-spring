package com.roadmaker.roadmap.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="ROADMAP_NODES")
public class RoadmapNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROADMAP_NODE_ID") @OneToMany
    private Long id;

    @JoinColumn(name = "ROADMAP_ID")
    @ManyToOne
    private Roadmap roadmap;

    @Column(name = "CLIENT_NODE_ID")
    private Long clientNodeId; //charflow의 노드 id

    @Column(name = "TYPE")
    private String type;

    @Column(name = "X_POSITION")
    private String xPosition;

    @Column(name = "Y_POSITION")
    private String yPosition;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "BACKGROUND")
    private String background;

    @Column(name = "BORDER")
    private String border;

    @Column(name = "BORDER_RADIUS")
    private Integer borderRadius;

    @Column(name = "FONT_SIZE")
    private Integer fontSize;

    @Column(name = "DETAILED_CONTENTS")
    private String detailedContents; //String 이 맞는지?

    @Builder
    public RoadmapNode(Long id, Roadmap roadmap, Long clientNodeId, String type, String xPosition, String yPosition, String label, String background, String border, Integer borderRadius, Integer fontSize, String detailedContents) {
        this.id = id;
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
        this.detailedContents = detailedContents;
    }
}
