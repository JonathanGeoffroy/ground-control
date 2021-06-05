package ground.control.planet.service;

import ground.control.planet.entities.Moon;
import ground.control.planet.repository.MoonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static java.util.UUID.randomUUID;

@Service
public class MoonService {
    private final MoonRepository repository;

    @Autowired
    public MoonService(MoonRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Moon create(Moon moon) {
        moon.setId(randomUUID().toString());
        return repository.save(moon);
    }
}
