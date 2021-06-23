package ground.control.planet.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class PlanetDTO extends RepresentationModel<PlanetDTO> {
  @NotNull private String id;
  @NotNull private String name;
  private Double size;
  private Double gravity;
}
