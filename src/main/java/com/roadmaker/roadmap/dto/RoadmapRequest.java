package com.roadmaker.roadmap.dto;

import com.roadmaker.member.entity.Member;
import com.roadmaker.roadmap.entity.roadmap.Roadmap;
import com.roadmaker.roadmap.entity.roadmapedge.RoadmapEdge;
import com.roadmaker.roadmap.entity.roadmapnode.RoadmapNode;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;


@Getter
@Setter
public class RoadmapRequest {
    @NotEmpty
    @Length(max = 100, message = "제목은 최대 100글자를 넘을 수 없습니다.")
    private String title;

    @NotEmpty
    private String description;

    private String thumbnailUrl;

    @NotEmpty
    private Integer recommendedExecutionTimeValue;

    @NotEmpty
    private String recommendedExecutionTimeUnit;

    private List<RoadmapNodeRequest> roadmapNodes;

    private List<RoadmapEdgeRequest> roadmapEdges;


    public Roadmap toEntity() {
        return Roadmap.builder()
                .title(this.title)
                .description(this.description)
                .thumbnailUrl(this.thumbnailUrl)
                .recommendedExecutionTimeValue(this.recommendedExecutionTimeValue)
                .recommendedExecutionTimeUnit(this.recommendedExecutionTimeUnit)
                .build();
    }
}
