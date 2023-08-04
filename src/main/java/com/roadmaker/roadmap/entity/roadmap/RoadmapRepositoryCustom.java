package com.roadmaker.roadmap.entity.roadmap;

import com.roadmaker.roadmap.dto.RoadmapFindResponse;
import org.springframework.data.domain.PageRequest;

public interface RoadmapRepositoryCustom {
    RoadmapFindResponse findBySearchOption(PageRequest pageRequest, String keyword);
    RoadmapFindResponse orderByLikes(PageRequest pageRequest);

}
