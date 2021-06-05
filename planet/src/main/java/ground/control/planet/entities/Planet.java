package ground.control.planet.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@NamedEntityGraph(
        name = Planet.PLANET_DETAILS_GRAPH,
        includeAllAttributes = true
)

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Planet {
    public static final String PLANET_DETAILS_GRAPH = "planet-details-graph";

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private Double size;

    @Column
    private Double gravity;

    @OneToMany(mappedBy = "planet", cascade = CascadeType.ALL)
    private List<Moon> moons;
}
