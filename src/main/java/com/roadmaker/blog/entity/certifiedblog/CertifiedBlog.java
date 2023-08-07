package com.roadmaker.blog.entity.certifiedblog;

import com.roadmaker.commons.BaseTimeEntity;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "CERTIFIED_BLOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CertifiedBlog extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "IN_PROGRESS_NODE_ID")
    private InProgressNode inProgressNode;

    @Column(columnDefinition = "TEXT")
    private String submitUrl;

    @Column
    private Boolean done;

    @Builder
    public CertifiedBlog(InProgressNode inProgressNode, String submitUrl, Boolean done) {
        this.inProgressNode = inProgressNode;
        this.submitUrl = submitUrl;
        this.done = done;
    }
}
