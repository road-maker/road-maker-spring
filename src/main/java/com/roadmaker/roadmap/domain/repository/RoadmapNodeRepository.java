package com.roadmaker.roadmap.domain.repository;

import com.roadmaker.roadmap.domain.entity.RoadmapNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapNodeRepository extends JpaRepository<RoadmapNode, Long> {
}
