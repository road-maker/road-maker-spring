package com.roadmaker.v1.member.dto.response;

import com.roadmaker.v1.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberFindResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String bio;
    private final String avatarUrl;
    private final String githubUrl;
    private final String blogUrl;

    @Builder
    private MemberFindResponse(Long id, String email, String nickname, String bio, String avatarUrl, String githubUrl, String blogUrl) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.githubUrl = githubUrl;
        this.blogUrl = blogUrl;
    }

    public static MemberFindResponse of(Member member) {
        return MemberFindResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .avatarUrl(member.getAvatarUrl())
                .githubUrl(member.getGithubUrl())
                .blogUrl(member.getBlogUrl())
                .build();
    }
}
