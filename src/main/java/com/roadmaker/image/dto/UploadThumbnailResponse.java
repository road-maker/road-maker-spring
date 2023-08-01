package com.roadmaker.image.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UploadThumbnailResponse {
    private String url;
}
