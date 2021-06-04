package ground.control.planet.service;

import ground.control.planet.entities.Planet;
import ground.control.planet.exception.NotFoundEntityException;
import ground.control.planet.repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.UUID.randomUUID;

@Service
public class PlanetService {
    private PlanetRepository repository;

    @Autowired
    public PlanetService(PlanetRepository repository) {
        this.repository = repository;
    }

    public List<Planet> getAll() {
        return repository.findAll();
    }

    public Planet create(Planet planet) {
        planet.setId(randomUUID().toString());
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
