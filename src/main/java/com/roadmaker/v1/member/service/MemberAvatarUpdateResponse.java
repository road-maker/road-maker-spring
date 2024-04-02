package com.roadmaker.v1.member.service;

import lombok.Builder;

@Builder
public record MemberAvatarUpdateResponse(String avatarUrl) {
}
