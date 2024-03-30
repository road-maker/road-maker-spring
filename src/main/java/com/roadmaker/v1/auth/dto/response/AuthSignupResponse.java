package com.roadmaker.v1.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthSignupResponse(Long id, String accessToken) {
}
