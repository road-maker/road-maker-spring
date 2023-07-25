package com.roadmaker.roadmap.entity.inprogressnode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.roadmaker.commons.BaseTimeEntity;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmap;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "IN_PROGRESS_NODE")
public class InProgressNode extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    @JsonBackReference
    private Roadmap roadmap;

    @ManyToOne
    @JoinColumn(name = "IN_PROGRESS_ROADMAP_ID")
    @JsonBackReference
    private InProgressRoadmap inProgressRoadmap;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_NODE_ID")
    @JsonBackReference
    private RoadmapNode roadmapNode;

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
