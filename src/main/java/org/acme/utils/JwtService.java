package org.acme.utils;

import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Qualifier;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    @ConfigProperty(name = "mp.jwt.private-key.location")
    String privateKeyLocation;


    public String generateToken(String username, String role) {
        return Jwt.issuer("tu-varna")
                .subject(username)
                .groups(role)
                .expiresIn(Duration.ofDays(7))
                .sign(privateKeyLocation);
    }
}
