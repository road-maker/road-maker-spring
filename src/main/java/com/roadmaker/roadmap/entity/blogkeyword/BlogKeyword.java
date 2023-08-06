package com.roadmaker.roadmap.entity.blogkeyword;

import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BLOG_KEYWORD")
public class BlogKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long roadmapNodeId;

    @Column
    private String keyword;

    @Builder
    public BlogKeyword(Long roadmapNodeId, String keyword) {
        this.roadmapNodeId = roadmapNodeId;
        this.keyword = keyword;
    }
}
