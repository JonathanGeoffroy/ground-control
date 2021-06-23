package ground.control.planet;

import ground.control.planet.entities.Moon;
import ground.control.planet.entities.Planet;
import ground.control.planet.repository.MoonRepository;
import ground.control.planet.repository.PlanetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlanetRestTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private PlanetRepository repository;

  @Autowired private MoonRepository moonRepository;

  @Test
  void getAll_empty() throws Exception {
    this.mockMvc
        .perform(get("/planet"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void getPaginated_order() throws Exception {
    var planetNames =
        new String[] {
          "c-planet", "a-planet", "d-planet", "b-planet",
        };
    for (String name : planetNames) {
      Planet planet = new Planet();
      planet.setName(name);
      repository.save(planet);
    }
    repository.flush();

    this.mockMvc
        .perform(get("/planet/paginated?size=3&page=0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].name", is("a-planet")))
        .andExpect(jsonPath("$[1].name", is("b-planet")))
        .andExpect(jsonPath("$[2].name", is("c-planet")));
  }

  @Test
  void getPaginated_firstPage() throws Exception {
    for (int i = 0; i < 8; i++) {
      Planet planet = new Planet();
      planet.setName("planet-" + i);
      repository.save(planet);
    }
    repository.flush();

    this.mockMvc
        .perform(get("/planet/paginated?size=4&page=0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$[0].name", is("planet-0")))
        .andExpect(jsonPath("$[1].name", is("planet-1")))
        .andExpect(jsonPath("$[2].name", is("planet-2")))
        .andExpect(jsonPath("$[3].name", is("planet-3")));
  }

  @Test
  void getPaginated_secondPage() throws Exception {
    for (int i = 0; i < 10; i++) {
      Planet planet = new Planet();
      planet.setName("planet-" + i);
      repository.save(planet);
    }
    repository.flush();

    this.mockMvc
        .perform(get("/planet/paginated?size=4&page=1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$[0].name", is("planet-4")))
        .andExpect(jsonPath("$[1].name", is("planet-5")))
        .andExpect(jsonPath("$[2].name", is("planet-6")))
        .andExpect(jsonPath("$[3].name", is("planet-7")));
  }

  @Test
  void getPaginated_lastPage_fullPage() throws Exception {
    for (int i = 0; i < 9; i++) {
      Planet planet = new Planet();
      planet.setName("planet-" + i);
      repository.save(planet);
    }
    repository.flush();

    this.mockMvc
        .perform(get("/planet/paginated?size=3&page=2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].name", is("planet-6")))
        .andExpect(jsonPath("$[1].name", is("planet-7")))
        .andExpect(jsonPath("$[2].name", is("planet-8")));
  }

  @Test
  void getPaginated_lastPage_notFullPage() throws Exception {
    for (int i = 0; i < 8; i++) {
      Planet planet = new Planet();
      planet.setName("planet-" + i);
      repository.save(planet);
    }
    repository.flush();

    this.mockMvc
        .perform(get("/planet/paginated?size=3&page=2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("planet-6")))
        .andExpect(jsonPath("$[1].name", is("planet-7")));
  }

  @Test
  void getPaginated_lastPage_outOfPage() throws Exception {
    for (int i = 0; i < 11; i++) {
      Planet planet = new Planet();
      planet.setName("planet-" + i);
      repository.save(planet);
    }
    repository.flush();

    this.mockMvc
        .perform(get("/planet/paginated?size=3&page=4"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void getAll_withPlanet() throws Exception {
    Planet planet = new Planet();
    planet.setName("Earth");
    planet.setGravity(9.807);
    planet.setSize(6371.0);
    repository.save(planet);

    this.mockMvc
        .perform(get("/planet"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(planet.getId())))
        .andExpect(jsonPath("$[0].name", is("Earth")))
        .andExpect(jsonPath("$[0].gravity", is(9.807)))
        .andExpect(jsonPath("$[0].size", is(6371.)))
        .andExpect(jsonPath("$[0].links[0].href", endsWith("/planet/" + planet.getId())))
        .andExpect(jsonPath("$.moons").doesNotExist());
  }

  @Test
  void getById() throws Exception {
    repository.save(new Planet("another-planet", "Another Planet", null, null, null));
    var planet = repository.save(new Planet("search-planet", "Planet we search", 1.23, 2.11, null));
    var firstMoon = moonRepository.save(new Moon("first-moon-id", "First Moon", planet));
    var secondMoon = moonRepository.save(new Moon("second-moon-id", "Second Moon", planet));

    this.mockMvc
        .perform(get("/planet/" + planet.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Planet we search")))
        .andExpect(jsonPath("$.id", is(planet.getId())))
        .andExpect(jsonPath("$.gravity", is(2.11)))
        .andExpect(jsonPath("$.size", is(1.23)))
        .andExpect(jsonPath("$.moons", hasSize(2)))
        .andExpect(
            jsonPath("$.moons[*].id", containsInAnyOrder(firstMoon.getId(), secondMoon.getId())))
        .andExpect(
            jsonPath(
                "$.moons[*].name", containsInAnyOrder(firstMoon.getName(), secondMoon.getName())));
  }

  @Test
  void getById_unknownPlanet() throws Exception {
    repository.save(new Planet("another-planet", "Another Planet", null, null, null));
    repository.save(new Planet("search-planet", "Planet we search", 1.23, 2.11, null));

    this.mockMvc.perform(get("/planet/unknown-planet")).andExpect(status().isNotFound());
  }

  @Test
  void addPlanet_minimumFields() throws Exception {
    this.mockMvc
        .perform(
            post("/planet").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Mars\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("Mars")))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.gravity").isEmpty())
        .andExpect(jsonPath("$.size").isEmpty());
  }

  @Test
  void addPlanet_allFields() throws Exception {
    this.mockMvc
        .perform(
            post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Earth\", \"gravity\": 9.807, \"size\": 6371}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("Earth")))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.gravity", is(9.807)))
        .andExpect(jsonPath("$.size", is(6371.0)));
  }

  @Test
  void addPlanet_throwEmptyName() throws Exception {
    this.mockMvc
        .perform(
            post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\", \"gravity\": 9.807, \"size\": 6371}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void addPlanet_throwNullName() throws Exception {
    this.mockMvc
        .perform(post("/planet").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void addPlanet_withMoon() throws Exception {
    this.mockMvc
        .perform(
            post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Earth\", \"moons\": [{ \"name\": \"moon\"}]}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("Earth")))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.moons[0].id").exists())
        .andExpect(jsonPath("$.moons[0].name", is("moon")))
        .andExpect(jsonPath("$.moons[0].planet").doesNotExist());
  }

  @Test
  void addPlanet_throwEmptyMoonName() throws Exception {
    this.mockMvc
        .perform(
            post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Earth\", \"moons\": [{ \"name\": \"\"}]}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void addPlanet_throwNullMoonName() throws Exception {
    this.mockMvc
        .perform(
            post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Earth\", \"moons\": [{}]}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deletePlanet() throws Exception {
    Planet planet = new Planet();
    planet.setName("Planet");
    repository.save(planet);

    this.mockMvc.perform(delete("/planet/" + planet.getId())).andExpect(status().isOk());

    this.mockMvc.perform(get("/planet/delete-me-id")).andExpect(status().isNotFound());
  }

  @Test
  void deletePlanet_withMoon() throws Exception {
    repository.save(new Planet("another-planet", "Another Planet", null, null, null));
    var planet = repository.save(new Planet("search-planet", "Planet we search", 1.23, 2.11, null));
    var firstMoon = moonRepository.save(new Moon("first-moon-id", "First Moon", planet));
    var secondMoon = moonRepository.save(new Moon("second-moon-id", "Second Moon", planet));

    this.mockMvc.perform(delete("/planet/" + planet.getId())).andExpect(status().isOk());

    this.mockMvc.perform(get("/planet/search-planet")).andExpect(status().isNotFound());

    Assertions.assertFalse(moonRepository.existsById(firstMoon.getId()));
    Assertions.assertFalse(moonRepository.existsById(secondMoon.getId()));
    Assertions.assertFalse(moonRepository.existsById(planet.getId()));
  }

  @Test
  void deletePlanet_unknownId() throws Exception {
    this.mockMvc.perform(delete("/planet/unknown-id")).andExpect(status().isNotFound());
  }
}
