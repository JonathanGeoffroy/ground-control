package ground.control.planet.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class PlanetDetailsDTO extends PlanetDTO {
  private List<MoonDTO> moons;
}
