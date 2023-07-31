package com.roadmaker.roadmap.dto;

import com.roadmaker.member.dto.MemberResponse;
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

    public static RoadmapDto of(Roadmap roadmap, Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. dd.");
        String createdAt = roadmap.getCreatedAt().format(formatter);
        String updatedAt = roadmap.getUpdatedAt().format(formatter);

        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .member(MemberResponse.of(member))
                .build();
    }
}
