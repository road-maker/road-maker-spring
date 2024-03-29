package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.member.dto.response.MemberResponse;
import com.roadmaker.v1.roadmap.entity.inprogressnode.InProgressNode;
import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
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
    private Boolean isLiked;
    private Integer joinCount;
    private Integer likeCount;
    private MemberResponse member;
    private String createdAt;
    private String updatedAt;

    private RoadmapViewportDto viewport;
    private List<RoadmapEdgeDto> edges;
    private List<RoadmapNodeDto> nodes;

    private static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. dd");
        return dateTime.format(formatter);
    }

    private static RoadmapResponse buildFromRoadmap(Roadmap roadmap, List<RoadmapNodeDto> nodes, boolean isJoined, boolean isLiked) {
        return RoadmapResponse.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .isJoined(isJoined)
                .isLiked(isLiked)
                .joinCount(roadmap.getInProgressRoadmapCount())
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
    public static RoadmapResponse of(Roadmap roadmap, Boolean isLiked) {
        return buildFromRoadmap(roadmap, roadmap.getRoadmapNodes().stream().map(RoadmapNodeDto::of).toList(), false, isLiked);
    }

    /** 로드맵에 참가한 사람의 DTO */
    public static RoadmapResponse of(Roadmap roadmap, Boolean isLiked, List<InProgressNode> inProgressNodes) {
        return buildFromRoadmap(roadmap, inProgressNodes.stream().map(RoadmapNodeDto::of).toList(), true, isLiked);
    }
}
