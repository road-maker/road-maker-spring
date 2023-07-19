package roadmap.domain.entity;

import com.roadmaker.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
