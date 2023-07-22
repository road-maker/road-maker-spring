package com.roadmaker.roadmap.entity.roadmapnode;

import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoadmapNodeRepository extends JpaRepository<RoadmapNode, Long> {
    public List<RoadmapNode> findByRoadmapId(Long roadmapId);
}