package com.roadmaker.v1.roadmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
@AllArgsConstructor
public class InProgressRoadmapDto {
    private Long id;
    private String roadmapId;
    private String title;
    private Boolean done;
    private String thumbnail;
    private double process;
}
