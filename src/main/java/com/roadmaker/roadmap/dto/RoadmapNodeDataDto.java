package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmapnodedata.RoadmapNodeData;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapNodeDataDto {
    private String label;

    public RoadmapNodeData toEntity() {
        return RoadmapNodeData.builder()
                .label(this.label)
                .build();
    }
}
