package ground.control.planet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CreatePlanetDTO {
  @NotNull @NotBlank private String name;

  private Double gravity;

  private Double size;

  @Valid private List<CreateMoonDTO> moons;
}
