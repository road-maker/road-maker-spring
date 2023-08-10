package com.roadmaker.roadmap.entity.blogkeyword;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String keyword = "";

    @Builder
    public BlogKeyword(String keyword) {
        this.keyword = keyword != null ? keyword : this.keyword;
    }
}
