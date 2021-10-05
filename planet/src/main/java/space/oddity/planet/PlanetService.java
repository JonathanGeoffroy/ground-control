package space.oddity.planet;

import space.oddity.planet.command.ElementCommand;
import space.oddity.planet.entities.Element;
import space.oddity.planet.entities.Planet;
import space.oddity.planet.exceptions.IllegalCompositionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class PlanetService {
  @Inject Random random;

  public List<Planet> page(int index, int size) {
    return Planet.page(index, size);
  }

  @Transactional
  public Planet create(Planet planet) {
    if (planet.getComposition() == null) {
      planet.setComposition(randomComposition());
    } else {
      checkComposition(planet.getComposition());
    }

    planet.persist();
    return planet;
  }

  private void checkComposition(Map<Element, Long> composition) {
    if (composition.values().stream().anyMatch(v -> v < 0)) {
      throw new IllegalCompositionException("Asked modification would result in negative number of element(s)");
    }
  }

  private Map<Element, Long> randomComposition() {
    var values = Element.values();
    var nbElements = random.nextInt(values.length);

    return IntStream.range(0, nbElements)
        .boxed()
        .collect(
            Collectors.toMap(
                i -> values[random.nextInt(values.length)], i -> positiveNumber(), (a, b) -> a));
  }

  private long positiveNumber() {
    return random.nextLong() & Long.MAX_VALUE;
  }

  public Planet find(String id) {
    return Planet.<Planet>findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  @Transactional
  public Planet updateComposition(String id, Map<Element, Long> composition) {
    checkComposition(composition);
    Planet planet = find(id);
    planet.getComposition().putAll(composition);
    return planet;
  }

  @Transactional
  public Planet commandComposition(String id, List<ElementCommand> commands) {
    Planet planet = find(id);

    for(ElementCommand command : commands) {
      command.act(planet.getComposition());
    }

    checkComposition(planet.getComposition());

    return planet;
  }
}
