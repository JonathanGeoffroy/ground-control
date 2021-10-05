package space.oddity.planet.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import space.oddity.planet.entities.Element;
import space.oddity.planet.exceptions.IllegalCompositionException;

import java.util.Map;

@AllArgsConstructor
@Data
public class SubtractElementCommand implements ElementCommand {

  private Element element;

  private Long value;

  @Override
  public Map<Element, Long> act(Map<Element, Long> composition) {
    Long old = composition.get(element);
    if (old == null) {
      throw new IllegalCompositionException(
          String.format("%s element is not present and can't be subtracted", element));
    }

    composition.put(element, old - value);

    return composition;
  }
}
