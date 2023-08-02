package com.roadmaker.image.controller;

import com.roadmaker.commons.annotation.LoginMember;
import com.roadmaker.commons.annotation.LoginRequired;
import com.roadmaker.image.dto.UploadThumbnailResponse;
import com.roadmaker.image.service.ImageService;
import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController()
@RequestMapping("/api/roadmaps")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final RoadmapService roadmapService;

    @LoginRequired
    @PostMapping("/{roadmapId}/thumbnails")
    public ResponseEntity<UploadThumbnailResponse> uploadThumbnail(
        @PathVariable Long roadmapId,
        @RequestPart(value = "file") MultipartFile multipartFile,
        @LoginMember Member member
    ) throws IOException {
        Roadmap roadmap = roadmapService.findRoadmapById(roadmapId);

        if (roadmap.getMember().getId().equals(member.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UploadThumbnailResponse uploadThumbnailResponse = imageService.uploadThumbnail(roadmap, multipartFile);

        return new ResponseEntity<>(uploadThumbnailResponse, HttpStatus.CREATED);
    }
}