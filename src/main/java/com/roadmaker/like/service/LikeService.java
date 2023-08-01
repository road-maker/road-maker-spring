package com.roadmaker.like.service;

import com.roadmaker.like.entity.Like;
import com.roadmaker.like.entity.LikeRepository;

public interface LikeService {

    // Check if a member has liked a roadmap
    public boolean isLiked(Long roadmapId, Long memberId);

    // Get the number of likes for a roadmap
    public int getLikeCount(Long roadmapId);

    // Toggle like for a member on a roadmap
    public void toggleLike(Long roadmapId, Long memberId);

    // Like a roadmap for a member
    public void likeRoadmap(Long roadmapId, Long memberId);

    // Unlike a roadmap for a member
    public void unlikeRoadmap(Long roadmapId, Long memberId);
}
