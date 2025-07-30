package org.acme.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.dto.ErrorDto;

@Provider
public class UserExceptionsMapper implements ExceptionMapper<UserExceptions> {

    @Override
    public Response toResponse(UserExceptions e) {
        ErrorDto errorDto = new ErrorDto(e.getClass().getSimpleName(), e.getMessage());
        return Response.status(e.getStatus()).entity(errorDto).build();
    }
}
