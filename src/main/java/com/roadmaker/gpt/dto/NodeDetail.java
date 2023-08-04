package com.roadmaker.gpt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NodeDetail {
    private String id;
    private String content;

    public NodeDetail(String content) {
        this.content = content;
    }
    public NodeDetail(String content, String id) {
        this.content = content;
        this.id = id;
    }
}
