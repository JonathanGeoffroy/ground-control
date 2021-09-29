package space.oddity.planet.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanetDTO {
  @NotNull @NotBlank private String name;

  private Double radius;

  private Double weight;

  private Double gravity;
}
