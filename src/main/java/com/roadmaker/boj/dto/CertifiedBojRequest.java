package com.roadmaker.boj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CertifiedBojRequest {

    private Long inProgressNodeId;

}
