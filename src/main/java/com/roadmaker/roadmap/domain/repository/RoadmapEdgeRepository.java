package com.roadmaker.roadmap.domain.repository;

import com.roadmaker.roadmap.domain.entity.RoadmapEdge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoadmapEdgeRepository extends JpaRepository<RoadmapEdge, Long> {
    public List<RoadmapEdge> findByRoadmapId(Long roadmapId);
}
