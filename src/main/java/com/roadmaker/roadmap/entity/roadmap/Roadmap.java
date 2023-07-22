package com.roadmaker.roadmap.entity.roadmap;

import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.roadmap.entity.roadmapeditor.RoadmapEditor;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROADMAP")
public class Roadmap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String thumbnailUrl;

    private Integer recommendedExecutionTimeValue;

    private String recommendedExecutionTimeUnit;

    @OneToMany(mappedBy = "roadmap")
    private List<RoadmapNode> roadmapNodes;

    @OneToMany(mappedBy = "roadmap")
    private List<RoadmapEdge> roadmapEdges;

    @OneToMany(mappedBy = "roadmap")
    private List<RoadmapEditor> roadmapEditors;

    @Builder
    public Roadmap(String title, String description, String thumbnailUrl, Integer recommendedExecutionTimeValue, String recommendedExecutionTimeUnit) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.recommendedExecutionTimeValue = recommendedExecutionTimeValue;
        this.recommendedExecutionTimeUnit = recommendedExecutionTimeUnit;
    }
}
