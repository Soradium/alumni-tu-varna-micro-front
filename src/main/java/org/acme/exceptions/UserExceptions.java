package org.acme.exceptions;

import jakarta.ws.rs.core.Response;

public abstract class UserExceptions extends RuntimeException {
    public UserExceptions(String message) {
        super(message);
    }

    public abstract Response.Status getStatus();

}
