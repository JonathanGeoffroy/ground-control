package ground.control.planet;

import ground.control.planet.entities.Planet;
import ground.control.planet.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PlanetRestTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlanetRepository repository;

    @Test
    public void getAll_empty() throws Exception {
        this.mockMvc.perform(get("/planet"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAll_withPlanet() throws Exception {
        Planet planet = new Planet();
        planet.setId("earth-planet");
        planet.setName("Earth");
        planet.setGravity(9.807);
        planet.setSize(6371.0);

        repository.save(planet);
        this.mockMvc.perform(get("/planet"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("earth-planet")))
                .andExpect(jsonPath("$[0].name", is("Earth")))
                .andExpect(jsonPath("$[0].gravity", is(9.807)))
                .andExpect(jsonPath("$[0].size", is(6371.)))
                .andExpect(jsonPath("$.moons").doesNotExist());
    }

    @Test
    public void addPlanet_minimumFields() throws Exception {
        this.mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Mars\"}"))

                .andExpect(status().isCreated()).andExpect(jsonPath("$.name", is("Mars")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.gravity").isEmpty())
                .andExpect(jsonPath("$.size").isEmpty());
    }

    @Test
    public void addPlanet_allFields() throws Exception {
        this.mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Earth\", \"gravity\": 9.807, \"size\": 6371}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Earth")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.gravity", is(9.807)))
                .andExpect(jsonPath("$.size", is(6371.0)));
    }

    @Test
    public void addPlanet_throwEmptyName() throws Exception {
        this.mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\", \"gravity\": 9.807, \"size\": 6371}"))

                .andExpect(status().isBadRequest());
    }

    @Test
    public void addPlanet_throwNullName() throws Exception {
        this.mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))

                .andExpect(status().isBadRequest());
    }

    @Test
    public void deletePlanet() throws Exception {
        Planet planet = new Planet();
        planet.setId("delete-me-id");
        planet.setName("Planet");
        repository.save(planet);

        this.mockMvc.perform(delete("/planet/delete-me-id"))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePlanet_unknownId() throws Exception {
        this.mockMvc.perform(delete("/planet/unknown-id"))
                .andExpect(status().isNotFound());
    }
}
