package com.roadmaker.roadmap.entity.blogkeyword;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "BLOG_KEYWORD")
public class BlogKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_NODE_ID")
    @JsonBackReference
    private RoadmapNode roadmapNode;

    private String keyword;

    @Builder
    public BlogKeyword(RoadmapNode roadmapNode, String keyword) {
        this.roadmapNode = roadmapNode;
        this.keyword = keyword;
    }
}
