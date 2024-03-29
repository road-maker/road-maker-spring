package com.roadmaker.v1.roadmap.entity.roadmapnode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.roadmaker.global.BaseTimeEntity;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.v1.roadmap.entity.roadmapnodedata.RoadmapNodeData;
import com.roadmaker.v1.roadmap.entity.roadmapnodeposition.RoadmapNodePosition;
import com.roadmaker.v1.roadmap.entity.roadmapnodepositionabsolute.RoadmapNodePositionAbsolute;
import com.roadmaker.v1.roadmap.entity.roadmapnodestyle.RoadmapNodeStyle;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROADMAP_NODE")
public class RoadmapNode extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    @JsonBackReference
    private Roadmap roadmap;

    private String clientNodeId;

    private Integer width;

    private Integer height;

    private String targetPosition;

    private String sourcePosition;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String detailedContent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROADMAP_NODE_STYLE_ID")
    private RoadmapNodeStyle style;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROADMAP_NODE_DATA_ID")
    private RoadmapNodeData data;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROADMAP_NODE_POSITION_ID")
    private RoadmapNodePosition position;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROADMAP_NODE_POSITION_ABSOLUTE_ID")
    private RoadmapNodePositionAbsolute positionAbsolute;

    @Builder
    public RoadmapNode(Roadmap roadmap, Integer width, Integer height, String sourcePosition, String targetPosition, String clientNodeId, String type, String detailedContent, RoadmapNodeStyle style, RoadmapNodeData data, RoadmapNodePosition position, RoadmapNodePositionAbsolute positionAbsolute) {
        this.roadmap = roadmap;
        this.clientNodeId = clientNodeId;
        this.width = width;
        this.height = height;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
        this.type = type;
        this.detailedContent = detailedContent;
        this.style = style;
        this.data = data;
        this.position = position;
        this.positionAbsolute = positionAbsolute;
    }
}
