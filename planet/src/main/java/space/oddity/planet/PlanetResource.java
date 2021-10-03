package space.oddity.planet;

import space.oddity.planet.dto.CreatePlanetDTO;
import space.oddity.planet.dto.PlanetDTO;
import space.oddity.planet.dto.PlanetDetailsDTO;
import space.oddity.planet.entities.Planet;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/planet/v1")
public class PlanetResource {

  @Inject PlanetService planetService;

  @Inject PlanetDTOMapper mapper;

  @GET
  @Path("/")
  public List<PlanetDTO> paginated(
      @DefaultValue("0") @QueryParam("index") int index,
      @DefaultValue("10") @QueryParam("size") int size) {
    return mapper.toDtoList(planetService.page(index, size));
  }

  @GET
  @Path("/{id}")
  public PlanetDetailsDTO details(@PathParam("id") UUID id) {
    return mapper.toDetailsDTO(planetService.find(id));
  }

  @POST
  @Transactional
  public Response create(@Valid @NotNull(message = "body must not be empty") CreatePlanetDTO dto) {
    Planet entity = planetService.create(mapper.toEntity(dto));
    return Response.created(URI.create("/planet/v1/" + entity.getId()))
        .entity(mapper.toDetailsDTO(entity))
        .build();
  }
}
