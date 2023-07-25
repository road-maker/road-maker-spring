package com.roadmaker.roadmap.entity.inprogressroadmap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InProgressRoadmapRepository extends JpaRepository<InProgressRoadmap, Long>
{
    public Optional<InProgressRoadmap> findByMemberId(Long member);

    public Optional<InProgressRoadmap> findByRoadmapIdAndMemberId(Long roadmapId, Long memberId);
}
