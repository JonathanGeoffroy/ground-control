package space.oddity.planet;

import space.oddity.planet.entities.Element;
import space.oddity.planet.entities.Planet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class PlanetService {
  @Inject private Random random;

  public List<Planet> page(int index, int size) {
    return Planet.page(index, size);
  }

  public Planet create(Planet planet) {
    if (planet.getComposition() == null) {
      planet.setComposition(randomComposition());
    }

    planet.persist();
    return planet;
  }

  private Map<Element, Long> randomComposition() {
    var values = Element.values();
    var nbElements = random.nextInt(values.length);

    return IntStream.range(0, nbElements)
        .boxed()
        .collect(
            Collectors.toMap(
                    i -> values[random.nextInt(values.length)],
                    i -> positiveNumber(),
                    (a, b) -> a));
  }

  private long positiveNumber() {
    return random.nextLong() & Long.MAX_VALUE;
  }

  public Planet find(UUID id) {
    return Planet.findById(id);
  }
}
