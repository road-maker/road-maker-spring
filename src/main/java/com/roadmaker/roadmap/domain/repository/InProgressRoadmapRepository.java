package com.roadmaker.roadmap.domain.repository;

import com.roadmaker.roadmap.domain.entity.InProgressRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InProgressRoadmapRepository extends JpaRepository<InProgressRoadmap, Long>
{
    public Optional<InProgressRoadmap> findByIdAndMemberId(Long id, Long member);
}
