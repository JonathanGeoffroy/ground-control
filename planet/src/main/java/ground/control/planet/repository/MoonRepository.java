package ground.control.planet.repository;

import ground.control.planet.entities.Moon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoonRepository extends JpaRepository<Moon, String> {
}
