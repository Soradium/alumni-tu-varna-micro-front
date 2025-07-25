package org.acme.utils;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
public class JwtServiceTest {

    @Inject
    JwtService jwtService;

    @Inject
    JWTParser parser;

    @ConfigProperty(name = "mp.jwt.verify.key")
    String secretKey;

    @Test
    public void correctTokenGeneration() throws ParseException {
        String token = jwtService.generateToken("admin", "admin");

        JsonWebToken rawToken = parser.verify(token, jwtService.getSignedKey(secretKey));

        assertEquals(rawToken.getSubject(), "admin");
        assertEquals(rawToken.getGroups(), Set.of("admin"));
        assertEquals(rawToken.getIssuer(), "tu-varna");
    }
}
