package com.roadmaker.roadmap.domain.entity;

import com.roadmaker.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "IN_PROGRESS_ROADMAP")
public class InProgressRoadmap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IN_PROGRESS_ROADMAP_ID")
    private Long id;

    @OneToMany
    @JoinColumn(name = "ROADMAP_ID")
    private Roadmap roadmap;

    @OneToMany
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy="IN_PROGRESS_NODE_ID")
    private List<InProgressNode> inProgressNodes;

    private Boolean done;

    @Builder
    public InProgressRoadmap(Roadmap roadmap, Member member, List<InProgressNode> inProgressNodes, Boolean done) {
        this.roadmap = roadmap;
        this.member = member;
        this.inProgressNodes = inProgressNodes;
        this.done = done;
    }
}
