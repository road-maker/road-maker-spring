package com.roadmaker.like.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LikeRoadmapResponse {
    private Boolean isLiked;
    private Integer likeCount;

    public static LikeRoadmapResponse from(boolean isLiked, int likeCount) {
        return LikeRoadmapResponse.builder()
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }
}
