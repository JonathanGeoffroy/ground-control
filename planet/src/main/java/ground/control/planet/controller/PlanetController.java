package ground.control.planet.controller;

import ground.control.planet.dto.CreatePlanetDTO;
import ground.control.planet.dto.PlanetDTO;
import ground.control.planet.dto.PlanetDetailsDTO;
import ground.control.planet.entities.Planet;
import ground.control.planet.exception.NotFoundEntityException;
import ground.control.planet.service.PlanetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/planet")
public class PlanetController {
    private final PlanetService service;
    private final ModelMapper modelMapper;

    @Autowired
    public PlanetController(PlanetService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PlanetDTO> getAll() {
        return service.getAll()
                .stream()
                .map(planet -> modelMapper.map(planet, PlanetDTO.class))
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public PlanetDetailsDTO getDetails(@PathVariable String id) throws NotFoundEntityException {
        var planet = service.findById(id);
        return modelMapper.map(planet, PlanetDetailsDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanetDetailsDTO create(@Valid @RequestBody CreatePlanetDTO dto) {
        var planet = service.create(modelMapper.map(dto, Planet.class));
        return modelMapper.map(planet, PlanetDetailsDTO.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws NotFoundEntityException {
        service.deleteById(id);
    }
}
