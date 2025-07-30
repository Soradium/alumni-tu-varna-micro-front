package org.acme.exceptions;

import jakarta.ws.rs.core.Response;

public class UserNotFoundException extends UserExceptions {
    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public Response.Status getStatus() {
        return Response.Status.NOT_FOUND;
    }
}
