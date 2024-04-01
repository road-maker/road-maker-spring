package com.roadmaker.v1.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByEmail(String email);
    public Optional<Member> findByNickname(String nickname);
    public boolean existsByEmail(String email);
    public boolean existsByNickname(String nickname);
}
