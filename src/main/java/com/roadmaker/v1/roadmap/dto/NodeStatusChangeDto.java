package com.roadmaker.v1.roadmap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class NodeStatusChangeDto {
    Long inProgressNodeId;
    Boolean done;
}
