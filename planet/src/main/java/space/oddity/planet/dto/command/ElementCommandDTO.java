package space.oddity.planet.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import space.oddity.planet.entities.Element;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class ElementCommandDTO {
  @NotNull private final CommandType command;

  @NotNull private final Element element;

  @NotNull private final Long value;

  public enum CommandType {
    ADD,
    SUB,
  }
}
