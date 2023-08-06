package com.roadmaker.roadmap.entity.roadmapnode;

import com.roadmaker.roadmap.entity.blogkeyword.BlogKeyword;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoadmapNodeRepository extends JpaRepository<RoadmapNode, Long> {
    public List<RoadmapNode> findByRoadmapId(Long roadmapId);

    public RoadmapNode findByRoadmapNodeId(Long roadmapNodeId);
}
