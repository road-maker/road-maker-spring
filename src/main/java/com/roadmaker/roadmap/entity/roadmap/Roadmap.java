package com.roadmaker.roadmap.entity.roadmap;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roadmaker.commons.BaseTimeEntity;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.roadmap.entity.roadmapeditor.RoadmapEditor;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROADMAP")
public class Roadmap extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    private String thumbnailUrl;

    private Integer recommendedExecutionTimeValue;  // 기능 추가 후 nullable=false 추가해야

    private String recommendedExecutionTimeUnit;   // 기능 추가 후 nullable=false 추가해야

    @OneToMany(mappedBy = "roadmap")
    @JsonManagedReference
    private List<RoadmapNode> roadmapNodes;

    @OneToMany(mappedBy = "roadmap")
    @JsonManagedReference
    private List<RoadmapEdge> roadmapEdges;

    @OneToMany(mappedBy = "roadmap")
    @JsonManagedReference
    private List<RoadmapEditor> roadmapEditors;

    @OneToOne
    @JoinColumn(name = "ROADMAP_VIEWPORT_ID")
    @Setter
    private RoadmapViewport roadmapViewport;


    @Builder
    public Roadmap(String title, String description, String thumbnailUrl, Integer recommendedExecutionTimeValue, String recommendedExecutionTimeUnit) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.recommendedExecutionTimeValue = recommendedExecutionTimeValue;
        this.recommendedExecutionTimeUnit = recommendedExecutionTimeUnit;
    }
}
