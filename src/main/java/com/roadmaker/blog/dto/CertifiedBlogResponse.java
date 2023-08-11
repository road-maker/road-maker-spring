package com.roadmaker.blog.dto;

import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CertifiedBlogResponse {
    private String submitUrl;
    private Boolean doneBlog;

    public static CertifiedBlogResponse of(CertifiedBlog certifiedBlog) {
        if (certifiedBlog == null) {
            throw new IllegalArgumentException("CertifiedBlog cannot be null");
        }
        return CertifiedBlogResponse.builder()
                .submitUrl(certifiedBlog.getSubmitUrl())
                .doneBlog(certifiedBlog.getDone())
                .build();
    }
}
