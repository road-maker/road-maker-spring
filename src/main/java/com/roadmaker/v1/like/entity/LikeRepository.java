package com.roadmaker.v1.like.entity;

import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    public boolean existsByRoadmapIdAndMemberId(Long roadmapId, Long memberId);
    public int countByRoadmapId(Long roadmapId);
    public Like findByRoadmapAndMember(Roadmap roadmap, Member member);
    public void deleteByRoadmapIdAndMemberId(Long roadmapId, Long memberId);
}
