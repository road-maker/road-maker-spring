package com.roadmaker.roadmap.entity.roadmap;


import org.springframework.data.jpa.repository.JpaRepository;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    public Roadmap findRoadmapById(Long id);
}
