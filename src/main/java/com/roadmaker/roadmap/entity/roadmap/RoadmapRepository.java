package com.roadmaker.roadmap.entity.roadmap;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    public Roadmap findRoadmapById(Long id);

    public List<Roadmap> findByMemberId(Long memberId);
}
