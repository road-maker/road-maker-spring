package roadmap.domain.entity;

import com.roadmaker.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "IN_PROGRESS_ROADMAP")
public class InProgressRoadmap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IN_PROGRESS_ROADMAP_ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "ROADMAP_ID")
    private Roadmap roadmap;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private Boolean done;
}
