package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmapnodedata.RoadmapNodeData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoadmapNodeDataDto {
    private String label;

    public RoadmapNodeData toEntity() {
        return RoadmapNodeData.builder()
                .label(this.label)
                .build();
    }
}
