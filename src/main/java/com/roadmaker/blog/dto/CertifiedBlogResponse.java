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

        String submitUrl = certifiedBlog.getSubmitUrl() != null ? certifiedBlog.getSubmitUrl() : "";
        Boolean doneBlog = certifiedBlog.getDone() != null ? certifiedBlog.getDone() : false;

        return CertifiedBlogResponse.builder()
                .submitUrl(submitUrl)
                .doneBlog(doneBlog)
                .build();
    }

}
