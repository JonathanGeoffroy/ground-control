package space.oddity.planet;

import space.oddity.planet.command.ElementCommand;
import space.oddity.planet.dto.PlanetDetailsDTO;
import space.oddity.planet.dto.command.ElementCommandDTO;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@Path("/planet/command/v1")
public class PlanetCommandResource {
  @Inject PlanetService planetService;

  @Inject PlanetDTOMapper mapper;

  @Inject ElementCommandMapper commandMapper;

  @PUT
  @Path("/{id}")
  @Transactional
  public PlanetDetailsDTO updateComposition(
      @NotNull(message = "id must be provided") @PathParam("id") String id,
      @Valid @NotNull(message = "body must not be empty") List<ElementCommandDTO> commandsDto) {

    List<ElementCommand> commands = commandsDto.stream().map(commandMapper::fromDto).toList();
    return mapper.toDetailsDTO(planetService.commandComposition(id, commands));
  }
}
