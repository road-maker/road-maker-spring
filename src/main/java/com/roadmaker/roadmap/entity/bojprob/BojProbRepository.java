package com.roadmaker.roadmap.entity.bojprob;

import com.roadmaker.roadmap.entity.blogkeyword.BlogKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BojProbRepository extends JpaRepository<BojProb, Long> {
    public BojProb findByRoadmapNodeId(Long roadmapNodeId);
}
