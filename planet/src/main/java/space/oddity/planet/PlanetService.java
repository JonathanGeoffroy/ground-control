package space.oddity.planet;

import space.oddity.planet.entities.Planet;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PlanetService {
  public List<Planet> page(int index, int size) {
    return Planet.page(index, size);
  }

  public Planet create(Planet planet) {
    planet.persist();
    return planet;
  }
}
