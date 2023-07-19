package roadmap.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ROADMAP_EDGE")
public class RoadmapEdge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROADMAP_EDGE")
    private Long id;

    private Long clientEdgeId;

    private String source;

    private String target;

    private String type;

    private Boolean isAnimated;
}
