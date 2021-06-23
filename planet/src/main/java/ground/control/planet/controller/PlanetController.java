package ground.control.planet.controller;

import ground.control.planet.dto.CreatePlanetDTO;
import ground.control.planet.dto.PlanetDTO;
import ground.control.planet.dto.PlanetDetailsDTO;
import ground.control.planet.entities.Planet;
import ground.control.planet.service.PlanetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/planet")
@Tag(name = "Index", description = "the Index API")
public class PlanetController {
  private final PlanetService service;
  private final ModelMapper modelMapper;

  @Autowired
  public PlanetController(PlanetService service, ModelMapper modelMapper) {
    this.service = service;
    this.modelMapper = modelMapper;
  }

  @Operation(summary = "Get all planets of the universe")
  @GetMapping(produces = "application/hal+json")
  public List<PlanetDTO> getAll() {
    return service.getAll().parallelStream()
        .map(
            planet ->
                modelMapper
                    .map(planet, PlanetDTO.class)
                    .add(
                        linkTo(methodOn(PlanetController.class).getDetails(planet.getId()))
                            .withSelfRel()))
        .collect(Collectors.toList());
  }

  @Operation(summary = "Paginated planets")
  @GetMapping(value = "/paginated", produces = "application/hal+json")
  public List<PlanetDTO> getPaginated(
      @RequestParam("page") int page, @RequestParam("size") int size) {
    return service.getPaginated(page, size).stream()
        .map(
            planet ->
                modelMapper
                    .map(planet, PlanetDTO.class)
                    .add(
                        linkTo(methodOn(PlanetController.class).getDetails(planet.getId()))
                            .withSelfRel()))
        .collect(Collectors.toList());
  }

  @Operation(summary = "Get planet details")
  @GetMapping("/{id}")
  public PlanetDetailsDTO getDetails(@PathVariable String id) {
    var planet = service.findById(id);
    return modelMapper.map(planet, PlanetDetailsDTO.class);
  }

  @Operation(summary = "Create a new planet")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PlanetDetailsDTO create(@Valid @RequestBody CreatePlanetDTO dto) {
    var planet = service.create(modelMapper.map(dto, Planet.class));
    return modelMapper.map(planet, PlanetDetailsDTO.class);
  }

  @Operation(summary = "Delete an existing planet")
  @DeleteMapping("/{id}")
  public void delete(@PathVariable String id) {
    service.deleteById(id);
  }
}
