package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.v1.roadmap.entity.roadmapnode.RoadmapNode;
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

    private Boolean done; // response에만 사용한다.

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

    public static RoadmapNodeDto of(RoadmapNode roadmapNode) {
        return RoadmapNodeDto.builder()
                .id(roadmapNode.getClientNodeId())
                .type(roadmapNode.getType())
                .width(roadmapNode.getWidth())
                .height(roadmapNode.getHeight())
                .sourcePosition(roadmapNode.getSourcePosition())
                .targetPosition(roadmapNode.getTargetPosition())
                .detailedContent(roadmapNode.getDetailedContent())
                .done(false)
                .style(RoadmapNodeStyleDto.of(roadmapNode.getStyle()))
                .data(RoadmapNodeDataDto.of(roadmapNode.getData()))
                .position(RoadmapNodePositionDto.of(roadmapNode.getPosition()))
                .positionAbsolute(RoadmapNodePositionAbsoluteDto.of(roadmapNode.getPositionAbsolute()))
                .build();
    }

    public static RoadmapNodeDto of(InProgressNode inProgressNode) {
        return RoadmapNodeDto.builder()
                .id(inProgressNode.getRoadmapNode().getClientNodeId())
                .type(inProgressNode.getRoadmapNode().getType())
                .width(inProgressNode.getRoadmapNode().getWidth())
                .height(inProgressNode.getRoadmapNode().getHeight())
                .sourcePosition(inProgressNode.getRoadmapNode().getSourcePosition())
                .targetPosition(inProgressNode.getRoadmapNode().getTargetPosition())
                .detailedContent(inProgressNode.getRoadmapNode().getDetailedContent())
                .done(inProgressNode.getDone())
                .style(RoadmapNodeStyleDto.of(inProgressNode.getRoadmapNode().getStyle()))
                .data(RoadmapNodeDataDto.of(inProgressNode.getRoadmapNode().getData()))
                .position(RoadmapNodePositionDto.of(inProgressNode.getRoadmapNode().getPosition()))
                .positionAbsolute(RoadmapNodePositionAbsoluteDto.of(inProgressNode.getRoadmapNode().getPositionAbsolute()))
                .build();
    }
}