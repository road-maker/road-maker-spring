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
    private Integer recommendedExecutionTimeValue;
    private String recommendedExecutionTimeUnit;
    private String ownerNickname;
    private String ownerAvatarUrl;

    public Roadmap toEntity() {
        return Roadmap.builder()
                .title(this.title)
                .description(this.description)
                .thumbnailUrl(this.thumbnailUrl)
                .recommendedExecutionTimeValue(this.recommendedExecutionTimeValue)
                .recommendedExecutionTimeUnit(this.recommendedExecutionTimeUnit)
                .build();
    }

    public static RoadmapDto of(Roadmap roadmap) {
        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .recommendedExecutionTimeValue(roadmap.getRecommendedExecutionTimeValue())
                .recommendedExecutionTimeUnit(roadmap.getRecommendedExecutionTimeUnit())
                .build();
    }

    public static RoadmapDto of(Roadmap roadmap, String ownerNickname, String ownerAvatarUrl) {
        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .description(roadmap.getDescription())
                .thumbnailUrl(roadmap.getThumbnailUrl())
                .recommendedExecutionTimeValue(roadmap.getRecommendedExecutionTimeValue())
                .recommendedExecutionTimeUnit(roadmap.getRecommendedExecutionTimeUnit())
                .ownerNickname(ownerNickname)
                .ownerAvatarUrl(ownerAvatarUrl)
                .build();
    }
}
