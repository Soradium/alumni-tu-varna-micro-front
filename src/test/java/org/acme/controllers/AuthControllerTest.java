package org.acme.controllers;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.AuthDto;
import org.acme.entities.User;
import org.acme.repository.UserRepository;
import org.acme.utils.BCryptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AuthControllerTest {

    @Inject
    UserRepository userRepository;

    @Inject
    BCryptService bcryptService;

    @BeforeEach
    @Transactional
    public void fakeUserRegistration(){
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bcryptService.hashPassword("admin123"));
        user.setRole("admin");

        userRepository.persistAndFlush(user);
    }

    @Test
    public void loginWithCorrectData() {
        AuthDto dto = new AuthDto();
        dto.setUsername("admin");
        dto.setPassword("admin123");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(200)
                    .body(notNullValue());
    }

    @Test
    public void loginWithIncorrectUsername() {
        AuthDto dto = new AuthDto();
        dto.setUsername("admin2");
        dto.setPassword("admin123");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(404)
                    .body("message", equalTo("UserNotFoundException"))
                    .body("error", equalTo("User admin2 not found"));
    }

    @Test
    public void loginWithIncorrectPassword() {
        AuthDto dto = new AuthDto();
        dto.setUsername("admin");
        dto.setPassword("admin1232");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(404)
                    .body("message", equalTo("SecurityException"))
                    .body("error", equalTo("Incorrect password"));
    }

    @Test
    public void loginWithoutUsername() {
        AuthDto dto = new AuthDto();
        dto.setUsername("");
        dto.setPassword("admin123");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(400)
                    .body("violations[0].message", equalTo("Username cannot be blank"));
    }

    @Test
    public void loginWithoutUsernameAndPassword() {
        AuthDto dto = new AuthDto();
        dto.setUsername("");
        dto.setPassword("");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(400)
                    .body("violations.message", hasItems("Username cannot be blank", "Password cannot be shorter than 6 characters"));
    }

    @Test
    public void loginWithShortPassword() {
        AuthDto dto = new AuthDto();
        dto.setUsername("admin");
        dto.setPassword("123");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(400)
                    .body("violations[0].message", equalTo("Password cannot be shorter than 6 characters"));
    }

    @Test
    public void registrationWithCorrectData() {
        AuthDto dto = new AuthDto();
        dto.setUsername("admin22");
        dto.setPassword("admin12345");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/register")
                .then()
                    .statusCode(200)
                    .body(notNullValue());
    }

    @Test
    public void registrationWithAlreadyExistingUsername() {
        AuthDto dto = new AuthDto();
        dto.setUsername("admin");
        dto.setPassword("admin12345");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/register")
                .then()
                    .statusCode(409)
                    .body("message", equalTo("UserAlreadyExist"))
                    .body("error", equalTo("User admin already exist"));
    }

    @Test
    public void registrationWithoutUsername() {
        AuthDto dto = new AuthDto();
        dto.setUsername("");
        dto.setPassword("admin12345");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/register")
                .then()
                    .statusCode(400)
                    .body("violations[0].message", equalTo("Username cannot be blank"));
    }

    @Test
    public void registrationWithoutPasswordAndUsername() {
        AuthDto dto = new AuthDto();
        dto.setUsername("");
        dto.setPassword("");

        RestAssured
                .given()
                    .contentType("application/json")
                    .body(dto)
                .when()
                    .post("/auth/register")
                    .then()
                .statusCode(400)
                    .body("violations.message", hasItems("Password cannot be shorter than 6 characters", "Username cannot be blank"));
    }
}
