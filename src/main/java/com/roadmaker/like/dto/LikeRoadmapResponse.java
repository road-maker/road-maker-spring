package com.roadmaker.like.dto;

import lombok.*;

@Builder
public record LikeRoadmapResponse(Boolean isLiked, Integer likeCount) {
    public static LikeRoadmapResponse from(boolean isLiked, int likeCount) {
        return LikeRoadmapResponse.builder()
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }
}
