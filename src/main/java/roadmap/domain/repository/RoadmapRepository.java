package roadmap.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import roadmap.domain.entity.Roadmap;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
}
