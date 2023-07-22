package com.roadmaker.roadmap.entity.inprogressroadmap;

import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "IN_PROGRESS_ROADMAP")
public class InProgressRoadmap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    private Roadmap roadmap;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy="inProgressRoadmap")
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