package ground.control.planet.service;

import ground.control.planet.entities.Moon;
import ground.control.planet.entities.Planet;
import ground.control.planet.exception.NotFoundEntityException;
import ground.control.planet.repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PlanetService {
    private final PlanetRepository repository;

    @Autowired
    public PlanetService(PlanetRepository repository) {
        this.repository = repository;
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
        if (!CollectionUtils.isEmpty(planet.getMoons())) {
            for (Moon moon : planet.getMoons()) {
                moon.setPlanet(planet);
            }
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
