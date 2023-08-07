package com.roadmaker.boj.dto;

import com.roadmaker.boj.entity.certifiedboj.CertifiedBoj;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CertifiedBojResponse {
    private String probNumber;
    private String probTitle;
    private Boolean done;

    public static CertifiedBojResponse of(CertifiedBoj certifiedBoj) {
        return CertifiedBojResponse.builder()
                .probNumber(String.valueOf(certifiedBoj.getInProgressNode().getRoadmapNode().getBojProb().getBojNumber()))
                .probTitle(certifiedBoj.getInProgressNode().getRoadmapNode().getBojProb().getBojTitle())
                .done(certifiedBoj.getDone())
                .build();
    }

}
