package space.oddity.planet;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;

public class Configuration {

  //@ConfigProperty(name = "app.random.seed")
  private Long seed;

  @ApplicationScoped
  public Random random() {
    if (seed != null) {
      return new Random(seed);
    }
    return new Random();
  }
}

