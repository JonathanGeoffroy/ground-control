package ground.control.planet.assembler;

import ground.control.planet.controller.PlanetController;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class PageAssembler<T, D extends RepresentationModel<?>>
    extends RepresentationModelAssemblerSupport<T, D> {

  PageAssembler(Class<?> controllerClass, Class<D> resourceType) {
    super(controllerClass, resourceType);
  }

  public PagedModel<D> toPagedModel(Page<T> paginated) {
    var content = paginated.stream().map(this::toModel).collect(Collectors.toList());

    var model =
        PagedModel.of(
            content,
            new PagedModel.PageMetadata(
                paginated.getSize(),
                paginated.getNumber(),
                paginated.getTotalElements(),
                paginated.getTotalPages()));

    model.addIf(
        paginated.hasNext(),
        () ->
            linkTo(
                    methodOn(PlanetController.class)
                        .getPaginated(
                            paginated.nextPageable().getPageNumber(), paginated.getSize()))
                .withRel("next"));

    model.addIf(
        paginated.hasPrevious(),
        () ->
            linkTo(
                    methodOn(PlanetController.class)
                        .getPaginated(
                            paginated.previousPageable().getPageNumber(), paginated.getSize()))
                .withRel("previous"));

    return model;
  }
}
