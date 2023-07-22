package com.roadmaker;

import com.roadmaker.member.entity.Member;
import com.roadmaker.member.entity.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Auditing 기능 적용")
    void findMember() {
        // given
        Member member = Member.builder()
                .nickname("haha")
                .email("haha@test.com")
                .password("hahaha")
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        System.out.println("savedMember.getCreatedAt() = " + savedMember.getCreatedAt());
        System.out.println("savedMember.getUpdatedAt() = " + savedMember.getUpdatedAt());

        //then
        Assertions.assertNotNull(savedMember.getCreatedAt());
        Assertions.assertNotNull(savedMember.getUpdatedAt());
    }

    @Test
    @DisplayName("updatedAt 적용")
    void updateMember() {

        // given
        Member member = Member.builder()
                .nickname("haha")
                .email("haha@test.com")
                .password("hahaha")
                .build();

        // when
        Member savedMember = memberRepository.save(member);
        LocalDateTime updatedAt01 = savedMember.getUpdatedAt();
        savedMember.setNickname("gagaga");
        memberRepository.flush();
        LocalDateTime updatedAt02 = savedMember.getUpdatedAt();

        //then
        Assertions.assertNotEquals(updatedAt01, updatedAt02);
    }
}
