package com.roadmaker.roadmap.entity.inprogressroadmap;

import com.roadmaker.roadmap.entity.inprogressroadmap.InProgressRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InProgressRoadmapRepository extends JpaRepository<InProgressRoadmap, Long>
{
    public Optional<InProgressRoadmap> findByIdAndMemberId(Long id, Long member);
}
