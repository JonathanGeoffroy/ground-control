package ground.control.planet.assembler;

import ground.control.planet.controller.PlanetController;
import ground.control.planet.dto.PlanetDTO;
import ground.control.planet.entities.Planet;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlanetAssembler extends PageAssembler<Planet, PlanetDTO> {
  private final ModelMapper modelMapper;

  @Autowired
  public PlanetAssembler(ModelMapper modelMapper) {
    super(PlanetController.class, PlanetDTO.class);
    this.modelMapper = modelMapper;
  }

  @Override
  @NonNull
  public PlanetDTO toModel(@NonNull Planet planet) {
    return modelMapper
        .map(planet, PlanetDTO.class)
        .add(linkTo(methodOn(PlanetController.class).getDetails(planet.getId())).withSelfRel());
  }
}
