package com.roadmaker.v1.roadmap.entity.inprogressnode;

import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InProgressNodeRepository extends JpaRepository<InProgressNode, Long> {
    public List<InProgressNode> findByRoadmapAndDone(Roadmap roadmapId, Boolean done);

    public List<InProgressNode> findByRoadmapIdAndMemberId(Long roadmapId, Long memberId);

    public InProgressNode findByMemberId(Long memberId);

    public InProgressNode findByRoadmapNodeId(Long roadmapNodeId);
}
