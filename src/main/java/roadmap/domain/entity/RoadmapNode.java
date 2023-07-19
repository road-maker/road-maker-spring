package roadmap.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ROADMAP_NODE")
public class RoadmapNode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    private Roadmap roadmap;

    private String clientNodeId;

    private String type;

    private Integer xPosition;

    private Integer yPosition;

    private String label;

    private String background;

    private String border;

    private Integer borderRadius;

    private Integer fontSize;
}
