package com.roadmaker.blog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class CertifiedBlogRequest {

    @NotNull(message = "inProgressNodeId cannot be null")
    private Long inProgressNodeId;

    @Builder.Default
    private final String submitUrl = "";
}
