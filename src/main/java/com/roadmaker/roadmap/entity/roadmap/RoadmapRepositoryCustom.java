package com.roadmaker.roadmap.entity.roadmap;

import com.roadmaker.roadmap.dto.RoadmapDto;
import com.roadmaker.roadmap.dto.RoadmapSearchResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RoadmapRepositoryCustom {
    RoadmapSearchResponse findyBySearchOption(PageRequest pageRequest, String keyword);

}
