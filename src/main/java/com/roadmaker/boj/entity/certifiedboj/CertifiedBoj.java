package com.roadmaker.boj.entity.certifiedboj;

import com.roadmaker.commons.BaseTimeEntity;
import com.roadmaker.roadmap.entity.inprogressnode.InProgressNode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "CERTIFIED_BOJ")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CertifiedBoj extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "IN_PROGRESS_NODE_ID")
    private InProgressNode inProgressNode;

    @Column
    private Boolean done;

    @Builder
    public CertifiedBoj(InProgressNode inProgressNode, Boolean done) {
        this.inProgressNode = inProgressNode;
        this.done = done;
    }
}
