package com.roadmaker.v1.roadmap.entity.roadmapedge;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.roadmaker.global.BaseTimeEntity;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROADMAP_EDGE")
public class RoadmapEdge extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    @JsonBackReference
    private Roadmap roadmap;

    private String clientEdgeId;

    private String source;

    private String target;

    private String type;

    private Boolean animated;

    @Builder
    public RoadmapEdge(Roadmap roadmap, String clientEdgeId, String source, String target, String type, Boolean animated) {
        this.roadmap = roadmap;
        this.clientEdgeId = clientEdgeId;
        this.source = source;
        this.target = target;
        this.type = type;
        this.animated = animated;
    }
}
