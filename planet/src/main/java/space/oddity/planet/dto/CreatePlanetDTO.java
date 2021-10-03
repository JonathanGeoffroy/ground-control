package space.oddity.planet.dto;

import lombok.*;
import space.oddity.planet.entities.Element;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanetDTO {
  @NotNull @NotBlank private String name;

  private Double radius;

  private Double weight;

  private Double gravity;

  private Map<Element, Long> composition;
}
