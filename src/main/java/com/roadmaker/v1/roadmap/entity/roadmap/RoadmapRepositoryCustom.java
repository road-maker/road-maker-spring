package com.roadmaker.v1.roadmap.entity.roadmap;

import com.roadmaker.v1.roadmap.dto.RoadmapFindResponse;
import org.springframework.data.domain.PageRequest;

public interface RoadmapRepositoryCustom {
    RoadmapFindResponse findBySearchOption(PageRequest pageRequest, String keyword);
    RoadmapFindResponse orderByLikes(PageRequest pageRequest);

}
