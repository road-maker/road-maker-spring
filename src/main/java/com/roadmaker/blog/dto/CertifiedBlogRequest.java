package com.roadmaker.blog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CertifiedBlogRequest {

    @NotNull
    private Long blogKeywordId;

    @Builder.Default
    private final String submitUrl = "";
}
