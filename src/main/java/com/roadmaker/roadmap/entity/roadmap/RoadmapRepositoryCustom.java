package com.roadmaker.roadmap.entity.roadmap;

import com.roadmaker.roadmap.dto.RoadmapDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RoadmapRepositoryCustom {
    List<RoadmapDto> findyBySearchOption(PageRequest pageRequest, String keyword);

}
