package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.roadmap.entity.roadmapnodedata.RoadmapNodeData;
import com.roadmaker.roadmap.entity.roadmapnodeposition.RoadmapNodePosition;
import com.roadmaker.roadmap.entity.roadmapnodepositionabsolute.RoadmapNodePositionAbsolute;
import com.roadmaker.roadmap.entity.roadmapnodestyle.RoadmapNodeStyle;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapNodeDto {
    private String id;
    private String type;

    private Integer width;
    private Integer height;

    private String sourcePosition;
    private String targetPosition;

    private String detailedContent;
    private RoadmapNodeStyleDto style;
    private RoadmapNodeDataDto data;
    private RoadmapNodePositionDto position;
    private RoadmapNodePositionAbsoluteDto positionAbsolute;


    public RoadmapNode toEntity(Roadmap roadmap) {
        return RoadmapNode.builder()
                .roadmap(roadmap)
                .width(this.width)
                .height(this.height)
                .sourcePosition(this.sourcePosition)
                .targetPosition(this.targetPosition)
                .clientNodeId(this.id)
                .type(this.type)
                .detailedContent(this.detailedContent)
                .style(this.style.toEntity())
                .data(this.data.toEntity())
                .position(this.position.toEntity())
                .positionAbsolute(this.positionAbsolute.toEntity())
                .build();
    }
}