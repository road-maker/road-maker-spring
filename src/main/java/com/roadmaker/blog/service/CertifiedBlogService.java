package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface CertifiedBlogService {
    void setKeyword(String keyword);
    void certifyBlog(CertifiedBlogRequest certifiedBlogRequest);

}
