package com.roadmaker.v1.image.dto;


import lombok.*;

@Getter
public class UploadImageResponse {
    private final String url;

    @Builder
    private UploadImageResponse(String url) {
        this.url = url;
    }
}
