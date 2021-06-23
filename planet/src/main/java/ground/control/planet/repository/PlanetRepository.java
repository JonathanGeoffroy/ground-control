package ground.control.planet.repository;

import ground.control.planet.entities.Planet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface PlanetRepository extends JpaRepository<Planet, String> {
  @EntityGraph(value = Planet.PLANET_DETAILS_GRAPH)
  @NonNull
  Optional<Planet> findById(@NonNull String id);

  @NonNull
  Page<Planet> findAll(@NonNull Pageable pageable);
}
