package com.roadmaker.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RoadmapData {
    @NotBlank
    String id;
    @NotBlank
    @JsonInclude() // content should always exist in the call, even if it is null
    String content;

    public RoadmapData() {

    }


    public RoadmapData(@NotBlank String id, @NotBlank String content) {
        this.id = id;
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(@NotBlank String content) {
        this.content = content;
    }
}
