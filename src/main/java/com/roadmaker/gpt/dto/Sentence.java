package com.roadmaker.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Sentence {
    String id;
    @JsonInclude() // content should always exist in the call, even if it is null
    String content;

    public Sentence() {

    }

    public Sentence(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
