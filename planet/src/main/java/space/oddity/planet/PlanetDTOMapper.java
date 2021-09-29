package space.oddity.planet;

import org.mapstruct.Mapper;
import space.oddity.planet.dto.CreatePlanetDTO;
import space.oddity.planet.dto.PlanetDTO;
import space.oddity.planet.dto.PlanetDetailsDTO;
import space.oddity.planet.entities.Planet;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface PlanetDTOMapper {
  PlanetDTO toDto(Planet planet);

  PlanetDetailsDTO toDetailsDTO(Planet planet);

  default List<PlanetDTO> toDtoList(List<Planet> planets) {
    return planets.stream().map(this::toDto).collect(Collectors.toList());
  }

  Planet toEntity(CreatePlanetDTO create);
}
