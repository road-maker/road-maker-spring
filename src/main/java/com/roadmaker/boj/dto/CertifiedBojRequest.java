package com.roadmaker.boj.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CertifiedBojRequest {

    private Long inProgressNodeId;

}
