package com.roadmaker.blog.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class CertifiedBlogRequest {

    private Long inProgressNodeId;

    private String submitUrl;
}
