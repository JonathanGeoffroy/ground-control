package ground.control.planet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class MoonDTO {
  @NotNull private String id;

  @NotNull private String name;
}
