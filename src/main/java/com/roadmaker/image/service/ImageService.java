package com.roadmaker.image.service;

import com.roadmaker.image.dto.UploadThumbnailResponse;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    public UploadThumbnailResponse uploadThumbnail(Roadmap roadmap, MultipartFile image) throws IOException;
}
