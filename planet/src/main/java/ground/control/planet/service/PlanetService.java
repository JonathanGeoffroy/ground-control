package ground.control.planet.service;

import ground.control.planet.entities.Planet;
import ground.control.planet.exception.NotFoundEntityException;
import ground.control.planet.repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Service
public class PlanetService {
    private final PlanetRepository repository;
    private final MoonService moonService;

    @Autowired
    public PlanetService(PlanetRepository repository, MoonService moonService) {
        this.repository = repository;
        this.moonService = moonService;
    }

    public List<Planet> getAll() {
        return repository.findAll();
    }

    public Planet findById(String planetId) throws NotFoundEntityException {
        var planet = repository.findById(planetId);

        if (planet.isEmpty()) {
            throw new NotFoundEntityException();
        }
        return planet.get();
    }

    @Transactional
    public Planet create(Planet planet) {
        planet.setId(randomUUID().toString());
        if (!CollectionUtils.isEmpty(planet.getMoons())) {
            planet.setMoons(
                    planet.getMoons()
                            .parallelStream()
                            .map(moonService::create)
                            .collect(Collectors.toList()));
        }

        return repository.save(planet);
    }

    public void deleteById(String planetId) throws NotFoundEntityException {
        try {
            repository.deleteById(planetId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundEntityException();
        }
    }
}
