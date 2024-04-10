package com.roadmaker.v1.roadmap.dto.response;

import com.roadmaker.v1.comment.entity.Comment;
import com.roadmaker.v1.member.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoadmapCommentPagingResponse(Long id, String content, String createdAt, Commenter member) {

    @Builder
    private record Commenter(Long id, String nickname, String avatarUrl) {
        public static Commenter of(Member member) {
            return Commenter.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .avatarUrl(member.getAvatarUrl())
                    .build();
        }
    }

    public static RoadmapCommentPagingResponse of(Comment comment) {
        return RoadmapCommentPagingResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toString())
                .member(Commenter.of(comment.getMember()))
                .build();
    }

}
