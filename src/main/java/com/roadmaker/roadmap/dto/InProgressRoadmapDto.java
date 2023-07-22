package com.roadmaker.roadmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class InProgressRoadmapDto {
    private String id;
    private String roadmapId;
    private String memberId;
    private Boolean done;
}
