package com.roadmaker.roadmap.entity.roadmapeditor;

import com.roadmaker.roadmap.dto.RoadmapResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapEditorRepository extends JpaRepository<RoadmapEditor, Long> {
    public RoadmapEditor findByRoadmapIdAndIsOwner(Long roadmapId, boolean isOwner);

    public List<RoadmapEditor> findAllByMemberIdAndIsOwner(Long memberId, boolean isOwner);

}
