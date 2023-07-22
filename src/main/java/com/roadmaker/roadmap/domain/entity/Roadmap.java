package com.roadmaker.roadmap.domain.entity;

import com.roadmaker.member.domain.entity.Member;
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
    @Column(name = "ROADMAP_ID")
    private Long id;

    private String title;

    private String description;

    private String thumbnailUrl;

    private Integer recommended_execution_time_value;

    private String recommended_execution_time_unit;

    @OneToMany(mappedBy = "roadmap")
    private List<RoadmapNode> roadmapNodes;

    @OneToMany(mappedBy = "roadmap")
    private List<RoadmapEdge> roadmapEdges;

    @OneToMany(mappedBy = "roadmap")
    private List<RoadmapEditor> roadmapEditors;

    @Builder
    public Roadmap(String title, String description, String thumbnailUrl, Integer recommended_execution_time_value, String recommended_execution_time_unit) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.recommended_execution_time_value = recommended_execution_time_value;
        this.recommended_execution_time_unit = recommended_execution_time_unit;
    }
}
