package com.roadmaker.roadmap.entity.roadmap;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roadmaker.commons.BaseTimeEntity;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.roadmap.entity.roadmapeditor.RoadmapEditor;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapviewport.RoadmapViewport;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROADMAP")
public class Roadmap extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String thumbnailUrl;

    private Integer recommendedExecutionTimeValue;

    private String recommendedExecutionTimeUnit;

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
