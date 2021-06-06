package ground.control.planet.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class PlanetDTO extends RepresentationModel<PlanetDTO> {
    private String id;
    private String name;
    private Double size;
    private Double gravity;
}
