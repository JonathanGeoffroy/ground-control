package space.oddity.planet.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanetDTO {
  @NotNull private String id;

  @NotNull private String name;

  private Double radius;

  private Double weight;

  private Double gravity;

  @NotNull
  private OffsetDateTime creationDate;
}
