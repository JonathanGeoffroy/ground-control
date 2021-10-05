package space.oddity.planet.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import space.oddity.planet.entities.Element;

import java.util.Map;

@AllArgsConstructor
@Data
public class AddElementCommand implements ElementCommand {

  private Element element;

  private Long value;

  @Override
  public Map<Element, Long> act(Map<Element, Long> composition) {
    composition.compute(element, (e, old) -> old == null ? value : old + value);
    return composition;
  }
}
