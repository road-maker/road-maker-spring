package com.roadmaker.v1.roadmap.dto;

import com.roadmaker.v1.roadmap.entity.roadmap.Roadmap;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Builder @Data @AllArgsConstructor
public class RoadmapNodeRequest {

    private Roadmap roadmap;

    @NotBlank
    private String clientNodeId;

    private String type;

    private Integer xPosition;

    private Integer yPosition;

    private String label;

    private String background;

    private String border;

    private Integer borderRadius;

    private Integer fontSize;

    @Length(max = 20000, message = "상세 내용은 최대 20000글자를 넘을 수 없습니다.")
    private String detailedContent;
}
