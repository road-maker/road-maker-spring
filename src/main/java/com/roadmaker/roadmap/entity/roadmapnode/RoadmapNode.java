package com.roadmaker.roadmap.entity.roadmapnode;

import com.roadmaker.commons.BaseTimeEntity;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapnodedata.RoadmapNodeData;
import com.roadmaker.roadmap.entity.roadmapnodeposition.RoadmapNodePosition;
import com.roadmaker.roadmap.entity.roadmapnodepositionabsolute.RoadmapNodePositionAbsolute;
import com.roadmaker.roadmap.entity.roadmapnodestyle.RoadmapNodeStyle;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROADMAP_NODE")
public class RoadmapNode extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    private Roadmap roadmap;

    private String clientNodeId;

    private String type;

    private Integer xPosition;

    private Integer yPosition;

    private String label;

    private String background;

    private String border;

    private Integer borderRadius;

    private Integer fontSize;

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
    public RoadmapNode(Roadmap roadmap, String clientNodeId, String type, Integer xPosition, Integer yPosition, String label, String background, String border, Integer borderRadius, Integer fontSize, String detailedContent, RoadmapNodeStyle style, RoadmapNodeData data, RoadmapNodePosition position, RoadmapNodePositionAbsolute positionAbsolute) {
        this.roadmap = roadmap;
        this.clientNodeId = clientNodeId;
        this.type = type;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.label = label;
        this.background = background;
        this.border = border;
        this.borderRadius = borderRadius;
        this.fontSize = fontSize;
        this.detailedContent = detailedContent;
        this.style = style;
        this.data = data;
        this.position = position;
        this.positionAbsolute = positionAbsolute;
    }
}
