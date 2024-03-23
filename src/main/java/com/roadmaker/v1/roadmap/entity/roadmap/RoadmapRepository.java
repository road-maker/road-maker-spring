package com.roadmaker.v1.roadmap.entity.roadmap;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long>, RoadmapRepositoryCustom {
    public Roadmap findRoadmapById(Long id);

    public List<Roadmap> findByMemberId(Long memberId, Sort option);
}
