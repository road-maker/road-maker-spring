package com.roadmaker.roadmap.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.roadmaker.roadmap.domain.entity.Roadmap;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
}
