package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.roadmap.entity.roadmapnodestyle.RoadmapNodeStyle;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
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

    public static RoadmapNodeStyleDto of(RoadmapNodeStyle roadmapNodeStyle) {
        return RoadmapNodeStyleDto.builder()
                .background(roadmapNodeStyle.getBackground())
                .border(roadmapNodeStyle.getBorder())
                .borderRadius(roadmapNodeStyle.getBorderRadius())
                .fontSize(roadmapNodeStyle.getFontSize())
                .build();
    }
}
