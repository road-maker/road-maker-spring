package com.roadmaker.v1.comment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.roadmaker.global.BaseTimeEntity;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "COMMENT")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    @JsonBackReference
    private Roadmap roadmap;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(length = 500, nullable = false)
    private String content;

    @Builder
    private Comment(Roadmap roadmap, Member member, String content) {
        this.roadmap = roadmap;
        this.member = member;
        this.content = content;
    }

    public static Comment create(String content, Roadmap roadmap, Member member) {
        return Comment.builder()
                .content(content)
                .roadmap(roadmap)
                .member(member)
                .build();
    }
}
