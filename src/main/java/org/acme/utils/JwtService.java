package org.acme.utils;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JwtService {
    public String generateToken(String username, String role) {
        return Jwt.issuer("tu-varna")
                .subject(username)
                .groups(role)
                .expiresIn(Duration.ofDays(7))
                .sign();
    }
}
