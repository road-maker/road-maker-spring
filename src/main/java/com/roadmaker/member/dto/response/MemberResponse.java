package com.roadmaker.member.dto.response;

import com.roadmaker.member.entity.Member;
import lombok.*;

@Getter
@ToString
public class MemberResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String bio;
    private final String avatarUrl;
    private final String githubUrl;
    private final String blogUrl;
    private final String baekjoonId;

    @Builder
    private MemberResponse(Long id, String email, String nickname, String bio, String avatarUrl, String githubUrl, String blogUrl, String baekjoonId) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.githubUrl = githubUrl;
        this.blogUrl = blogUrl;
        this.baekjoonId = baekjoonId;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .avatarUrl(member.getAvatarUrl())
                .githubUrl(member.getGithubUrl())
                .blogUrl(member.getBlogUrl())
                .baekjoonId(member.getBaekjoonId())
                .build();
    }
}
