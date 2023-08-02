package com.roadmaker.roadmap.entity.roadmap;

import com.roadmaker.roadmap.dto.RoadmapFindResponse;
import org.springframework.data.domain.PageRequest;

public interface RoadmapRepositoryCustom {
    RoadmapFindResponse findyBySearchOption(PageRequest pageRequest, String keyword);

}
