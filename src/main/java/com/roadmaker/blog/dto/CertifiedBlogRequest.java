package com.roadmaker.blog.dto;

import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CertifiedBlogRequest {
    private String submitUrl;
    private String keyword;
    private InProgressNode inProgressNode;
}
