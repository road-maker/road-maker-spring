package com.roadmaker.roadmap.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="ROADMAP_EDGES")
public class RoadmapEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROADMAP_EDGE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="ROADMAP_ID")
    private Roadmap roadmap;

    @Column(name = "CLIENT_EDGE_ID")
    private Long clientEdgeId;

    @Column(name = "SOURCE") //source 노드의 id? - 타입 확인
    private Long source;

    @Column(name = "TARGET") //target 노드의 id? - 타입 확인
    private Long target;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "IS_ANIMATED")
    private Boolean isAnimated;

    @Builder
    public RoadmapEdge(Long id, Roadmap roadmap, Long clientEdgeId, Long source, Long target, String type, Boolean isAnimated) {
        this.id = id;
        this.roadmap = roadmap; //
        this.clientEdgeId = clientEdgeId;
        this.source = source;
        this.target = target;
        this.type = type;
        this.isAnimated = isAnimated;
    }
}
