package com.roadmaker.v1.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthSignupRequest(@NotBlank String email, @NotBlank String password, @NotBlank String nickname) {
}
