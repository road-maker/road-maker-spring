package com.roadmaker.v1.roadmap.entity.roadmap;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roadmaker.global.BaseTimeEntity;
import com.roadmaker.v1.inprogressroadmap.entity.InProgressRoadmap;
import com.roadmaker.v1.like.entity.Like;
import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.v1.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.v1.roadmap.entity.roadmapviewport.RoadmapViewport;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROADMAP")
public class Roadmap extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 500)
    private String description;

    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "roadmap")
    private List<Like> likes;

    @OneToMany(mappedBy = "roadmap")
    private List<InProgressRoadmap> inProgressRoadmaps;

    @OneToMany(mappedBy = "roadmap")
    @JsonManagedReference
    private List<RoadmapNode> roadmapNodes;

    @OneToMany(mappedBy = "roadmap")
    @JsonManagedReference
    private List<RoadmapEdge> roadmapEdges;

    @OneToOne
    @JoinColumn(name = "ROADMAP_VIEWPORT_ID")
    @Setter
    private RoadmapViewport roadmapViewport;

    @Builder
    public Roadmap(String title, String description, String thumbnailUrl, Member member) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.member = member;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public int getInProgressRoadmapCount() {
        return inProgressRoadmaps.size();
    }

    public void updateThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isOwner(Long memberId) {
        return this.member.getId().equals(memberId);
    }
}
