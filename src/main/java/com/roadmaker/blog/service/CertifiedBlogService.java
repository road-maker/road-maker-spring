package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;

public interface CertifiedBlogService {
    void setKeyword(String keyword, RoadmapNode roadmapNodeId);
    void certifyBlog(CertifiedBlogRequest certifiedBlogRequest);
}
