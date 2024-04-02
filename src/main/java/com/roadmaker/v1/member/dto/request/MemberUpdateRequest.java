package com.roadmaker.v1.member.dto.request;

import lombok.Builder;

@Builder
public record MemberUpdateRequest(String nickname, String bio, String blogUrl, String githubUrl) {
}
