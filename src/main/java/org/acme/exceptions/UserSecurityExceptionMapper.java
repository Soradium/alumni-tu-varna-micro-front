package org.acme.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.dto.ErrorDto;

@Provider
public class UserSecurityExceptionMapper implements ExceptionMapper<SecurityException> {
    @Override
    public Response toResponse(SecurityException e) {
        ErrorDto errorDto = new ErrorDto(e.getClass().getSimpleName(), e.getMessage());
        return Response.status(404).entity(errorDto).build();
    }
}
