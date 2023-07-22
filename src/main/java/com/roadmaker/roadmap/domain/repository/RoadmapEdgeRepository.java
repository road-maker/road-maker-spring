package com.roadmaker.roadmap.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.roadmaker.roadmap.domain.entity.RoadmapEdge;

public interface RoadmapEdgeRepository extends JpaRepository<RoadmapEdge, Long> {
}
