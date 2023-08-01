package com.roadmaker.blog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CertifiedBlogResponse {
    private String submitUrl;
    private Boolean doneBlog;

    public CertifiedBlogResponse(String submitUrl, boolean doneBlog) {
        this.submitUrl = submitUrl;
        this.doneBlog = doneBlog;
    }
}
