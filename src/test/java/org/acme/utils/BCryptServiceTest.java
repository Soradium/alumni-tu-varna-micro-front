package org.acme.utils;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BCryptServiceTest {

    @Inject
    BCryptService bcryptService;

    @Test
    public void correctPasswordHashAndVerification() {
        String password = "password";
        String hashedPassword = bcryptService.hashPassword(password);

        assertNotNull(hashedPassword);
        assertTrue(bcryptService.verifyPassword(password, hashedPassword));
    }

    @Test
    public void wrongPasswordHashAndVerification() {
        String password = "password";
        String wrongPassword = "wrongPassword";
        String hashedPassword = bcryptService.hashPassword(password);

        assertNotNull(hashedPassword);
        assertFalse(bcryptService.verifyPassword(wrongPassword, hashedPassword));
    }
}
