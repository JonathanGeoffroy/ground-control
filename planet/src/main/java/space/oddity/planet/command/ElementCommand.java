package space.oddity.planet.command;

import space.oddity.planet.entities.Element;

import java.util.Map;

public interface ElementCommand {
    Map<Element, Long> act(Map<Element, Long> composition);
}
