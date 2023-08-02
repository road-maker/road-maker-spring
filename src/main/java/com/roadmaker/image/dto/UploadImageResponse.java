package com.roadmaker.image.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UploadImageResponse {
    private String url;
}
