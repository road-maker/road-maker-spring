package com.roadmaker.image.service;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    @Transactional
    public String uploadImage(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);

        String uuidImageName = UUID.randomUUID().toString() + "." + extension;

        S3Resource s3Resource = s3Template.upload(bucketName, uuidImageName, image.getInputStream(), ObjectMetadata.builder().contentType(extension).build());

        return s3Resource.getURL().toString();
    }
}
