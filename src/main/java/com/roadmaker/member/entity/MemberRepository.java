package com.roadmaker.member.entity;

import com.roadmaker.blog.entity.certifiedblog.CertifiedBlog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByEmail(String email);
    public Optional<Member> findByNickname(String nickname);
    public Optional<Member> findByBlogUrl(String blogUrl);
}
