package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.roadmap.entity.roadmapnodedata.RoadmapNodeData;
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

    public static RoadmapNodeDataDto of(RoadmapNodeData roadmapNodeData) {
        return RoadmapNodeDataDto.builder()
                .label(roadmapNodeData.getLabel())
                .build();
    }
}
