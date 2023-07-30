package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import lombok.*;

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
    private String ownerNickname;
    private String ownerAvatarUrl;

    public Roadmap toEntity() {
        return Roadmap.builder()
                .title(this.title)
                .description(this.description)
                .thumbnailUrl(this.thumbnailUrl)
                .build();
    }

    public static RoadmapDto of(Roadmap roadmap) {
        if (roadmap == null) { // null이 입력되는 경우 처리
            return null;
        }
        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .build();
    }

    public static RoadmapDto of(Roadmap roadmap, String ownerNickname) {
        if (roadmap == null) {
            return null;
        }
        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .ownerNickname(ownerNickname)
//                .ownerAvatarUrl(ownerAvatarUrl)
                .build();
    }
}
