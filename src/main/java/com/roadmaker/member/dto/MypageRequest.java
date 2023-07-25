package com.roadmaker.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MypageRequest {
    private Long memberId;
    private String nickname;
    private String bio;
    private String avatarUrl;
    private String githubUrl;
    private String blogUrl;
    private String baekjoonId;
}
