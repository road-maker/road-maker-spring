package com.roadmaker.v1.roadmap.entity.roadmapedge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapEdgeRepository extends JpaRepository<RoadmapEdge, Long> {
    public List<RoadmapEdge> findByRoadmapId(Long roadmapId);

}
