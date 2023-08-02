package com.roadmaker.roadmap.dto;

import com.roadmaker.member.dto.MemberResponse;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private Integer joinCount;
    private Integer likeCount;
    private MemberResponse member;
    private String createdAt;
    private String updatedAt;

    private RoadmapViewportDto viewport;
    private List<RoadmapEdgeDto> edges;
    private List<RoadmapNodeDto> nodes;

    private static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. dd.");
        return dateTime.format(formatter);
    }

    private static RoadmapResponse buildFromRoadmap(Roadmap roadmap, Integer joinCount, List<RoadmapNodeDto> nodes, boolean isJoined) {
        return RoadmapResponse.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .isJoined(isJoined)
                .joinCount(joinCount)
                .likeCount(roadmap.getLikeCount())
                .member(MemberResponse.of(roadmap.getMember()))
                .createdAt(formatDate(roadmap.getCreatedAt()))
                .updatedAt(formatDate(roadmap.getUpdatedAt()))
                .viewport(RoadmapViewportDto.of(roadmap.getRoadmapViewport()))
                .nodes(nodes)
                .edges(roadmap.getRoadmapEdges().stream().map(RoadmapEdgeDto::of).toList())
                .build();
    }

    /** 로그인 하지 않거나, 로드맵에 참가하지 않은 사람의 DTO */
    public static RoadmapResponse of(Roadmap roadmap, Integer joinCount) {
        return buildFromRoadmap(roadmap, joinCount, roadmap.getRoadmapNodes().stream().map(RoadmapNodeDto::of).toList(), false);
    }

    /** 로드맵에 참가한 사람의 DTO */
    public static RoadmapResponse of(Roadmap roadmap, Integer joinCount, List<InProgressNode> inProgressNodes) {
        return buildFromRoadmap(roadmap, joinCount, inProgressNodes.stream().map(RoadmapNodeDto::of).toList(), true);
    }
}
