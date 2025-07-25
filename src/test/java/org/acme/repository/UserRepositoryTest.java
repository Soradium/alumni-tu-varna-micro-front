package org.acme.repository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
public class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    @BeforeEach
    @Transactional
    public void cleanup(){
        userRepository.deleteAll();
    }

    @Test
    public void testIfUserExists() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin123");

        userRepository.persistAndFlush(user);

        Optional<User> found = userRepository.findByUsername("admin");
        assertTrue(found.isPresent());
        assertEquals("admin", found.get().getUsername());
    }

    @Test
    public void testIfUserNotExists() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin123");

        userRepository.persistAndFlush(user);

        Optional<User> found = userRepository.findByUsername("alex");
        assertFalse(found.isPresent());
    }
}
