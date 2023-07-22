package com.roadmaker.roadmap.entity.roadmapedge;

import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapEdgeRepository extends JpaRepository<RoadmapEdge, Long> {
    public List<RoadmapEdge> findByRoadmapId(Long roadmapId);
}
