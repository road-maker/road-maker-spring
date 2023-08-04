package com.roadmaker.blog.entity.blogkeyword;

import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "BLOG_KEYWORD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROADMAP_NODE")
    private RoadmapNode roadmapnode;

    @Column(columnDefinition = "TEXT")
    private String keyword;

    @Builder
    public BlogKeyword(RoadmapNode roadmapNode, String keyword){
        this.roadmapnode = roadmapNode;
        this.keyword = keyword;
    }
}
