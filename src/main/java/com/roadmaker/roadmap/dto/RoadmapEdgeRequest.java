package com.roadmaker.roadmap.dto;

import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import jakarta.persistence.*;

public class RoadmapEdgeRequest {

    private Roadmap roadmap;

    private String clientEdgeId;

    private String source;

    private String target;

    private String type;

    private Boolean isAnimated;
}
