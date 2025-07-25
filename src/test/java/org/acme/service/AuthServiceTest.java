package org.acme.service;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.AuthDto;
import org.acme.entities.User;
import org.acme.exceptions.UserAlreadyExist;
import org.acme.exceptions.UserNotFoundException;
import org.acme.repository.UserRepository;
import org.acme.utils.BCryptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
public class AuthServiceTest {

    @Inject
    AuthService authService;

    @Inject
    BCryptService bcryptService;

    @Inject
    UserRepository userRepository;

    @BeforeEach
    @Transactional
    public void cleanup(){
        userRepository.deleteAll();
    }

    @Test
    public void loginWithCorrectData() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bcryptService.hashPassword("admin1234"));
        user.setRole("admin");

        userRepository.persistAndFlush(user);

        AuthDto authDto = new AuthDto();
        authDto.setUsername(user.getUsername());
        authDto.setPassword("admin1234");

        assertDoesNotThrow(() -> {
            String token = authService.login(authDto);
            assertNotNull(token);
        });
    }

    @Test
    public void loginWithIncorrectUsername() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bcryptService.hashPassword("admin1234"));
        user.setRole("admin");

        userRepository.persistAndFlush(user);

        AuthDto authDto = new AuthDto();
        authDto.setUsername("ivan");
        authDto.setPassword("admin1234");

        assertThrows(
                UserNotFoundException.class,
                () -> authService.login(authDto)
        );
    }

    @Test
    public void loginWithIncorrectPassword() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bcryptService.hashPassword("admin1234"));
        user.setRole("admin");

        userRepository.persistAndFlush(user);

        AuthDto authDto = new AuthDto();
        authDto.setUsername(user.getUsername());
        authDto.setPassword("admin123");

        assertThrows(
                SecurityException.class,
                () -> authService.login(authDto)
        );
    }

    @Test
    public void registrationWithCorrectData() {
        AuthDto authDto = new AuthDto();
        authDto.setUsername("admin");
        authDto.setPassword("admin1234");

        assertDoesNotThrow(() -> {
            authService.register(authDto);
        });
    }

    @Test
    public void registrationWithAlreadyExistingUsername() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bcryptService.hashPassword("admin1234"));
        user.setRole("admin");

        userRepository.persistAndFlush(user);

        AuthDto authDto = new AuthDto();
        authDto.setUsername("admin");
        authDto.setPassword("admin1234");

        assertThrows(
                UserAlreadyExist.class,
                () -> authService.register(authDto)
        );
    }
}
