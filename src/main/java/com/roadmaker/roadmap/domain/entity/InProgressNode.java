package com.roadmaker.roadmap.domain.entity;

import com.roadmaker.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "IN_PROGRESS_NODE")
public class InProgressNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IN_PROGRESS_NODE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    private Roadmap roadmap;

    @ManyToOne
    @JoinColumn(name = "IN_PROGRESS_ROADMAP_ID")
    private InProgressRoadmap inProgressRoadmap;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_NODE_ID")
    RoadmapNode roadmapNode;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private Boolean done;

    @Builder
    public InProgressNode(Roadmap roadmap, RoadmapNode roadmapNode, Member member, Boolean done) {
        this.roadmap = roadmap;
        this.roadmapNode = roadmapNode;
        this.member = member;
        this.done = done;
    }
}
