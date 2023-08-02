package com.roadmaker.like.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LikeRoadmapResponse {
    private Boolean like;
    private Integer likeCount;

    public static LikeRoadmapResponse from(boolean like, int likeCount) {
        return LikeRoadmapResponse.builder()
                .like(like)
                .likeCount(likeCount)
                .build();
    }
}
