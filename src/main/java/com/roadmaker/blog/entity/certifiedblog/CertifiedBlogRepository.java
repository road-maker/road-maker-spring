package com.roadmaker.blog.entity.certifiedblog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertifiedBlogRepository extends JpaRepository<CertifiedBlog, Long> {
}
