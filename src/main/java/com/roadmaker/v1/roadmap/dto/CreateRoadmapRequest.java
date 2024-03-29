package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.member.entity.Member;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.v1.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.v1.roadmap.entity.roadmapnode.RoadmapNode;
import com.roadmaker.v1.roadmap.entity.roadmapnodedata.RoadmapNodeData;
import com.roadmaker.v1.roadmap.entity.roadmapnodeposition.RoadmapNodePosition;
import com.roadmaker.v1.roadmap.entity.roadmapnodepositionabsolute.RoadmapNodePositionAbsolute;
import com.roadmaker.v1.roadmap.entity.roadmapnodestyle.RoadmapNodeStyle;
import com.roadmaker.v1.roadmap.entity.roadmapviewport.RoadmapViewport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateRoadmapRequest {
    @Valid @NotNull
    private RoadmapDto roadmap;

    @Valid @NotNull
    private ViewportDto viewport;

    @Valid @NotNull @NotEmpty
    private List<RoadmapEdgeDto> edges;

    @Valid @NotNull @NotEmpty
    private List<RoadmapNodeDto> nodes;


    @Getter
    @Builder
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class RoadmapDto {
        @NotBlank
        private String title;

        @NotBlank
        private String description;

        private String thumbnailUrl;

        private Member member;

        public Roadmap toEntity(Member member) {
            return Roadmap.builder()
                    .title(this.title)
                    .description(this.description)
                    .thumbnailUrl(this.thumbnailUrl)
                    .member(member)
                    .build();
        }
    }


    @Getter
    @Builder
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class ViewportDto {
        @NotNull
        private BigDecimal x;

        @NotNull
        private BigDecimal y;

        @NotNull
        private BigDecimal zoom;

        public RoadmapViewport toEntity() {
            return RoadmapViewport.builder()
                    .x(this.x)
                    .y(this.y)
                    .zoom(this.zoom)
                    .build();
        }
    }


    @Getter
    @Builder
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class RoadmapEdgeDto {
        @NotBlank
        private String id;

        @NotBlank
        private String source;

        @NotBlank
        private String target;

        @NotBlank
        private String type;

        @NotNull
        private Boolean animated;

        public RoadmapEdge toEntity(Roadmap roadmap) {
            return RoadmapEdge.builder()
                    .clientEdgeId(this.id)
                    .roadmap(roadmap)
                    .source(this.source)
                    .target(this.target)
                    .type(this.type)
                    .animated(this.animated)
                    .build();
        }
    }


    @Getter
    @Builder
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class RoadmapNodeDto {
        @NotBlank
        private String id;

        @NotBlank
        private String type;

        @NotNull
        private Integer width;

        @NotNull
        private Integer height;

        @NotBlank
        private String sourcePosition;

        @NotBlank
        private String targetPosition;

        private String detailedContent;

        @Valid @NotNull
        private StyleDto style;

        @Valid @NotNull
        private DataDto data;

        @Valid @NotNull
        private PositionDto position;

        @Valid @NotNull
        private PositionAbsoluteDto positionAbsolute;

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

        @Getter
        @ToString
        @Builder
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @AllArgsConstructor
        public static class StyleDto {
            @NotBlank
            private String background;

            @NotBlank
            private String border;

            @NotNull
            private Integer borderRadius;

            @NotNull
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

        @Getter
        @ToString
        @Builder
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @AllArgsConstructor
        public static class DataDto {
            @NotBlank
            private String label;

            public RoadmapNodeData toEntity() {
                return RoadmapNodeData.builder()
                        .label(this.label)
                        .build();
            }

        }

        @Getter
        @ToString
        @Builder
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @AllArgsConstructor
        public static class PositionDto {
            @NotNull
            private Integer x;

            @NotNull
            private Integer y;

            public RoadmapNodePosition toEntity() {
                return RoadmapNodePosition.builder()
                        .x(this.x)
                        .y(this.y)
                        .build();
            }
        }

        @Getter
        @ToString
        @Builder
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @AllArgsConstructor
        public static class PositionAbsoluteDto {
            @NotNull
            private Integer x;

            @NotNull
            private Integer y;

            public RoadmapNodePositionAbsolute toEntity() {
                return RoadmapNodePositionAbsolute.builder()
                        .x(this.x)
                        .y(this.y)
                        .build();
            }
        }
    }
}
