package com.roadmaker.roadmap.entity.blogkeyword;

import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogKeywordRepository extends JpaRepository<BlogKeyword, String> {
    public List<BlogKeyword> findByRoadmapNode(RoadmapNode roadmapNode);
}
