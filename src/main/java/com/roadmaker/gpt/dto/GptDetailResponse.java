package com.roadmaker.gpt.dto;

public class GptDetailResponse {
    private String content;

    public GptDetailResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
