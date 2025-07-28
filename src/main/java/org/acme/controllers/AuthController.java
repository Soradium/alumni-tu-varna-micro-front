package org.acme.controllers;

import avro.alumni.AlumniSchemaDto;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.AuthDto;
import org.acme.service.AuthService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class AuthController {
    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid AuthDto authDto) {
        String token = authService.login(authDto);
        return Response.ok(token).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    public Response register(@Valid AuthDto authDto) {
        authService.register(authDto);
        return Response.ok().build();
    }
}
