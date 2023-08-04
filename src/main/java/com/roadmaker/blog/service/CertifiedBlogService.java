package com.roadmaker.blog.service;

import com.roadmaker.blog.dto.CertifiedBlogResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface CertifiedBlogService {
    // 본인 블로그 검증 로직
    public CertifiedBlogResponse checkUrl(String submitUrl, String blogUrl);
    // 블로그 키워드 검증 로직

}
