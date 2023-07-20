package roadmap.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ROADMAP_EDGE")
public class RoadmapEdge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROADMAP_EDGE")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    private Roadmap roadmap;

    @OneToMany(mappedBy = "roadmapNode")
    private List<InProgressNode> inProgressNodes;

    private String clientEdgeId;

    private String source;

    private String target;

    private String type;

    private Boolean isAnimated;
}
