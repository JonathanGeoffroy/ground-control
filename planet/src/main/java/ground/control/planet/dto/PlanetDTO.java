package ground.control.planet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlanetDTO {
    private String id;
    private String name;
    private Double size;
    private Double gravity;
}
