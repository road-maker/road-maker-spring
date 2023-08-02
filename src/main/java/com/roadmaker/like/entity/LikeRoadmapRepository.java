package com.roadmaker.like.entity;

import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRoadmapRepository extends JpaRepository<LikeRoadmap, Long> {
    public boolean existsByRoadmapIdAndMemberId(Long roadmapId, Long memberId);
    public int countByRoadmapId(Long roadmapId);
    public Like findByRoadmapAndMember(Roadmap roadmap, Member member);
    public void deleteByRoadmapIdAndMemberId(Long roadmapId, Long memberId);
}
