package com.roadmaker.roadmap.entity.bojprob;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOJ_PROB")
public class BojProb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long roadmapNodeId;

    @Column
    private Integer bojNumber;

    @Column
    private String bojTitle;

    @Builder
    public BojProb(Long roadmapNodeId, Integer bojNumber, String bojTitle) {
        this.roadmapNodeId = roadmapNodeId;
        this.bojNumber = bojNumber;
        this.bojTitle = bojTitle;
    }
}
