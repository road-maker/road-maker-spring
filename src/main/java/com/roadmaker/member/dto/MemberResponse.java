package com.roadmaker.member.dto;

import com.roadmaker.member.entity.Member;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String email;
    private String nickname;
    private String bio;
    private String avatarUrl;
    private String githubUrl;
    private String blogUrl;
    private String baekjoonId;
//    private Integer level;
//    private Integer exp;

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
//                .level(member.getLevel())
//                .exp(member.getExp())
                .build();
    }
}
