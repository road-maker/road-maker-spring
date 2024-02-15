package com.roadmaker.comment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.roadmaker.global.BaseTimeEntity;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
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
    Comment(Roadmap roadmap, Member member, String content) {
        this.roadmap = roadmap;
        this.member = member;
        this.content = content;
    }
}
