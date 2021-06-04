package ground.control.planet.repository;

import ground.control.planet.entities.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository  extends JpaRepository<Planet, String> {
}
