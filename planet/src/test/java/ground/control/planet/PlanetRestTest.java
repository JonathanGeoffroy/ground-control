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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        repository.save((new Planet("earth-planet", "Earth")));
        this.mockMvc.perform(get("/planet"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("earth-planet")))
                .andExpect(jsonPath("$[0].name", is("Earth")));
    }

    @Test
    public void addPlanet() throws Exception {
        this.mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Mars\"}"))

                .andExpect(status().isCreated()).andExpect(jsonPath("$.name", is("Mars")))
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void addPlanet_throwEmptyName() throws Exception {
        this.mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\"}"))

                .andExpect(status().isBadRequest());
    }

    @Test
    public void addPlanet_throwNullName() throws Exception {
        this.mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))

                .andExpect(status().isBadRequest());
    }
}
