package space.oddity.planet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalCompositionHandler implements ExceptionMapper<IllegalCompositionException> {
  @Override
  public Response toResponse(IllegalCompositionException exception) {
    return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(exception.getMessage())).build();
  }

  @AllArgsConstructor
  @Data
  @Builder
  static class ErrorDTO {
    private final String error;
  }
}
