package com.roadmaker.blog.dto;

import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import com.roadmaker.member.dto.MemberResponse;
import com.roadmaker.member.entity.Member;
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
        return CertifiedBlogResponse.builder()
                .submitUrl(certifiedBlog.getSubmitUrl())
                .doneBlog(certifiedBlog.getDone())
                .build();
    }
}
