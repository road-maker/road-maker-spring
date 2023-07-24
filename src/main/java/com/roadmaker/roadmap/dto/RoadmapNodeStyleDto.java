package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmapnodestyle.RoadmapNodeStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoadmapNodeStyleDto {
    private String background;
    private String border;
    private Integer borderRadius;
    private Integer fontSize;

    public RoadmapNodeStyle toEntity() {
        return RoadmapNodeStyle.builder()
                .background(this.background)
                .border(this.border)
                .borderRadius(this.borderRadius)
                .fontSize(this.fontSize)
                .build();
    }
}
