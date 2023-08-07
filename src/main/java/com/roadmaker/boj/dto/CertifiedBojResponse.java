package com.roadmaker.boj.dto;

import com.roadmaker.boj.entity.certifiedboj.CertifiedBoj;
import com.roadmaker.boj.entity.certifiedboj.CertifiedBojRepository;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class CertifiedBojResponse {
    private String probNumber;
    private String probTitle;
    private Boolean done;

}
