package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.acme.dto.AuthDto;
import org.acme.entities.User;
import org.acme.exceptions.UserAlreadyExist;
import org.acme.exceptions.UserNotFoundException;
import org.acme.repository.UserRepository;
import org.acme.utils.BCryptService;
import org.acme.utils.JwtService;

import java.lang.annotation.Inherited;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;
    @Inject
    JwtService jwtService;
    @Inject
    BCryptService bcryptService;

    public String login( AuthDto authDto) {
        User user = userRepository.findByUsername(authDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User " + authDto.getUsername() + " not found"));

        if(!bcryptService.verifyPassword(authDto.getPassword(), user.getPassword())){
            throw new SecurityException("Incorrect password");
        }
        return jwtService.generateToken(authDto.getUsername(), user.getRole());
    }


    @Transactional
    public void register(AuthDto authDto) {
        if (userRepository.findByUsername(authDto.getUsername()).isPresent()) {
            throw new UserAlreadyExist("User " + authDto.getUsername() +  " already exist");
        }

        User user = new User();

        user.setPassword(bcryptService.hashPassword(authDto.getPassword()));
        user.setUsername(authDto.getUsername());
        user.setRole("ADMIN");

        userRepository.persistAndFlush(user);
    }

}
