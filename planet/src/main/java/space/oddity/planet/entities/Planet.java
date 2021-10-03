package space.oddity.planet.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Planet extends PanacheEntityBase {
  private static final String CREATION_DATE_FIELD = "creationDate";

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @Column private String name;

  @Column private Double weight;

  @Column private Double radius;

  @Column private Double gravity;

  @Column(name = CREATION_DATE_FIELD)
  @NotNull
  @CreationTimestamp
  private OffsetDateTime creationDate;

  @ElementCollection
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  @MapKeyEnumerated(EnumType.STRING)
  private Map<Element, Long> composition;

  public static List<Planet> page(int index, int size) {
    return findAll(Sort.by(CREATION_DATE_FIELD, Sort.Direction.Descending))
        .page(Page.of(index, size))
        .list();
  }
}
