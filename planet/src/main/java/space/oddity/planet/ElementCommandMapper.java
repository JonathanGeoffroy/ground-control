package space.oddity.planet;

import space.oddity.planet.command.AddElementCommand;
import space.oddity.planet.command.ElementCommand;
import space.oddity.planet.command.SubtractElementCommand;
import space.oddity.planet.dto.command.ElementCommandDTO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ElementCommandMapper {

  public ElementCommand fromDto(ElementCommandDTO dto) {
    return switch (dto.getCommand()) {
      case ADD -> new AddElementCommand(dto.getElement(), dto.getValue());
      case SUB -> new SubtractElementCommand(dto.getElement(), dto.getValue());
    };
  }
}
