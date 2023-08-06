package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogRequest;
import com.roadmaker.blog.dto.CertifiedBlogResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface CertifiedBlogService {
    CertifiedBlogResponse certifyBlog(CertifiedBlogRequest certifiedBlogRequest);
}
