package ground.control.planet.repository;

import ground.control.planet.entities.Planet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanetRepository extends JpaRepository<Planet, String> {
  @EntityGraph(value = Planet.PLANET_DETAILS_GRAPH)
  Optional<Planet> findById(String id);
}
