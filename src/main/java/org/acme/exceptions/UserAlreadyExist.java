package org.acme.exceptions;

import jakarta.ws.rs.core.Response;

public class UserAlreadyExist extends UserExceptions {
    public UserAlreadyExist(String message) {
        super(message);
    }

    @Override
    public Response.Status getStatus() {
        return Response.Status.CONFLICT;
    }
}
