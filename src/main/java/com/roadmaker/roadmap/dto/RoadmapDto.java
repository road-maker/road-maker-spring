package com.roadmaker.roadmap.dto;

import com.roadmaker.member.dto.response.MemberResponse;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoadmapDto {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Integer likeCount;
    private Integer joinCount;
    private MemberResponse member;
    private String createdAt;
    private String updatedAt;

    public Roadmap toEntity(Member member) {
        return Roadmap.builder()
                .title(this.title)
                .description(this.description)
                .thumbnailUrl(this.thumbnailUrl)
                .member(member)
                .build();
    }

    private static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. dd");
        return dateTime.format(formatter);
    }

    public static RoadmapDto of(Roadmap roadmap, Member member) {

        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .createdAt(formatDate(roadmap.getCreatedAt()))
                .updatedAt(formatDate(roadmap.getUpdatedAt()))
                .member(MemberResponse.of(member))
                .likeCount(roadmap.getLikeCount())
                .joinCount(roadmap.getInProgressRoadmapCount())
                .build();
    }
}
