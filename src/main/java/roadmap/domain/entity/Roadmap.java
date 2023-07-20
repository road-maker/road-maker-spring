package roadmap.domain.entity;

import com.roadmaker.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ROADMAP")
public class Roadmap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROADMAP_ID")
    private Long id;

    private String title;

    private String description;

    private String thumbnailUrl;

    private Integer recommended_execution_time_value;

    private String recommended_execution_time_unit;

    @OneToMany(mappedBy = "roadmap")
    private List<RoadmapNode> roadmapNodes;

    @OneToMany(mappedBy = "roadmap")
    private List<RoadmapEdge> roadmapEdges;

    @OneToMany(mappedBy = "roadmap")
    private List<Member> roadmapEditors;

    @Builder
    public Roadmap(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
