package ground.control.planet.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@NamedEntityGraph(name = Planet.PLANET_DETAILS_GRAPH, includeAllAttributes = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Planet {
  public static final String PLANET_DETAILS_GRAPH = "planet-details-graph";

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @Column private String name;

  @Column private Double size;

  @Column private Double gravity;

  @OneToMany(mappedBy = "planet", cascade = CascadeType.ALL)
  private Set<Moon> moons;
}
