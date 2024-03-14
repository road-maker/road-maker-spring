package com.roadmaker.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberUpdateRequest {
    private Long memberId;
    @NotBlank
    private String nickname;
    private String bio;
    private String blogUrl;
    private String baekjoonId;
}
