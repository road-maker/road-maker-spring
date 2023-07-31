package com.roadmaker.roadmap.dto;

import com.roadmaker.member.dto.MemberResponse;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Boolean isJoined;
    private MemberResponse member;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private RoadmapViewportDto viewport;
    private List<RoadmapEdgeDto> edges;
    private List<RoadmapNodeDto> nodes;

    /** 로그인 하지 않거나, 로드맵에 참가하지 않은 사람의 DTO */
    public static RoadmapResponse of(Roadmap roadmap) {
        return RoadmapResponse.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .isJoined(false)
                .member(MemberResponse.of(roadmap.getMember()))
                .createdAt(roadmap.getCreatedAt())
                .updatedAt(roadmap.getUpdatedAt())
                .viewport(RoadmapViewportDto.of(roadmap.getRoadmapViewport()))
                .nodes(roadmap.getRoadmapNodes().stream().map(RoadmapNodeDto::of).toList())
                .edges(roadmap.getRoadmapEdges().stream().map(RoadmapEdgeDto::of).toList())
                .build();
    }

    /** 로드맵에 참가한 사람의 DTO */
    public static RoadmapResponse of(Roadmap roadmap, List<InProgressNode> inProgressNodes) {
        return RoadmapResponse.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .isJoined(true)
                .member(MemberResponse.of(roadmap.getMember()))
                .createdAt(roadmap.getCreatedAt())
                .updatedAt(roadmap.getUpdatedAt())                .viewport(RoadmapViewportDto.of(roadmap.getRoadmapViewport()))
                .nodes(inProgressNodes.stream().map(RoadmapNodeDto::of).toList())
                .edges(roadmap.getRoadmapEdges().stream().map(RoadmapEdgeDto::of).toList())
                .build();
    }
}
