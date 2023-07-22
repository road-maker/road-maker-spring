package com.roadmaker.roadmap.domain.repository;

import com.roadmaker.roadmap.domain.entity.RoadmapNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoadmapNodeRepository extends JpaRepository<RoadmapNode, Long> {
    public List<RoadmapNode> findByRoadmapId(Long roadmapId);
}
