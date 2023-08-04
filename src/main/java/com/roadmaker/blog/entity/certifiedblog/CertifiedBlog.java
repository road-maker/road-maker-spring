package com.roadmaker.blog.entity.certifiedblog;

import com.roadmaker.commons.BaseTimeEntity;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "CERTIFIED_BLOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class CertifiedBlog extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "IN_PROGRESS_NODE_ID")
    private InProgressNode inProgressNode;

    @Column(columnDefinition = "TEXT")
    private String blogUrl;

    @Column(columnDefinition = "TEXT")
    private String submitUrl;

    private String keyword;

    @Builder
    public CertifiedBlog(InProgressNode inProgressNode, Member member, String submitUrl, String blogUrl, String keyword) {
        this.member = member;
        this.inProgressNode = inProgressNode;
        this.submitUrl = submitUrl;
        this.blogUrl = blogUrl;
        this.keyword = keyword;
    }
}
