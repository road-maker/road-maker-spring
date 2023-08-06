package com.roadmaker.gpt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NodeDetail {
    private String id;
    private String detailedContent;

    public NodeDetail(String content) {
        this.detailedContent = content;
    }
    public NodeDetail(String content, String id) {
        this.detailedContent = content;
        this.id = id;
    }
}
