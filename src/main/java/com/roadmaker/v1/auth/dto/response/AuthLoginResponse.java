package com.roadmaker.v1.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthLoginResponse(Long id, String accessToken) {
    public static AuthLoginResponse of(Long id, String accessToken) {
        return AuthLoginResponse.builder()
                .id(id)
                .accessToken(accessToken)
                .build();
    }
}
