package space.oddity.planet.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import space.oddity.planet.entities.Element;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PlanetDetailsDTO extends PlanetDTO {
  Map<Element, Long> composition;
}
