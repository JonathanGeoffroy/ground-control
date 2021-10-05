package space.oddity.planet;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import space.oddity.planet.entities.Planet;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Random;


public class AbstractRunner {
    @Inject
    Random random;

    @Transactional
    static void empty() {
        for (var p : Planet.findAll().list()) {
            p.delete();
        }
    }

    @BeforeEach
    void mock() {
        random = Mockito.mock(Random.class);
        QuarkusMock.installMockForType(random, Random.class);
    }

    @AfterEach
    void resetAfter() {
        empty();
    }
}
