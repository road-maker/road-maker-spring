package com.roadmaker.roadmap.entity.inprogressroadmap;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roadmaker.commons.BaseTimeEntity;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "IN_PROGRESS_ROADMAP")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InProgressRoadmap extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    @JsonBackReference
    private Roadmap roadmap;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    @JsonBackReference
    private Member member;

    @OneToMany(mappedBy="inProgressRoadmap")
    @JsonManagedReference
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
