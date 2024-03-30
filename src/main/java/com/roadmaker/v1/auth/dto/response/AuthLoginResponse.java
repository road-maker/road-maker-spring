package com.roadmaker.v1.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthLoginResponse(Long id, String accessToken) {
}
