package com.roadmaker.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

public class GptRoadmapResponse {
    @NotBlank
    String id;
    @NotBlank
    @JsonInclude() // content should always exist in the call, even if it is null
    String content;

    public GptRoadmapResponse() {

    }


    public GptRoadmapResponse(@NotBlank String id, @NotBlank String content) {
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

    public void setContent(@NotBlank String content) {
        this.content = content;
    }
}
