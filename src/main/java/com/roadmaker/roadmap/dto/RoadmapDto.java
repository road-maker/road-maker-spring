package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoadmapDto {
    private String title;
    private String description;
    private String thumbnailUrl;
    private Integer recommendedExecutionTimeValue;
    private String recommendedExecutionTimeUnit;

    public Roadmap toEntity() {
        return Roadmap.builder()
                .title(this.title)
                .description(this.description)
                .thumbnailUrl(this.thumbnailUrl)
                .recommendedExecutionTimeValue(this.recommendedExecutionTimeValue)
                .recommendedExecutionTimeUnit(this.recommendedExecutionTimeUnit)
                .build();
    };
}
