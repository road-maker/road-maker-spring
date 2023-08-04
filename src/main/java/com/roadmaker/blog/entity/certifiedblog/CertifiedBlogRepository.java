package com.roadmaker.blog.entity.certifiedblog;

import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertifiedBlogRepository extends JpaRepository<CertifiedBlog, Long> {
    // Member와 연관된 CertifiedBlog 목록을 조회하는 메서드
    List<CertifiedBlog> findByMemberId(Member member);

    // InProgressNode와 연관된 CertifiedBlog 목록을 조회하는 메서드
    List<CertifiedBlog> findByInProgressNodeId(InProgressNode inProgressNode);
}
