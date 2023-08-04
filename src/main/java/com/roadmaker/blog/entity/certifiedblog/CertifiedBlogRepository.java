package com.roadmaker.blog.entity.certifiedblog;

import com.roadmaker.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertifiedBlogRepository  extends JpaRepository<CertifiedBlog, Long> {
    public List<CertifiedBlog> findByBlogUrl(String blogUrl);
}
