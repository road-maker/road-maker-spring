package com.roadmaker.boj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class CertifiedBojResponse {
    private String probNumber;
    private String probTitle;
    private Boolean done;

}
