package org.acme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthDto {
    @NotNull(message = "Username cannot be blank")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotNull(message = "Password cannot be blank")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password cannot be shorter than 6 characters")
    private String password;

    public AuthDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthDto() {
    }

    public @NotNull(message = "Username cannot be blank") @NotBlank(message = "Username cannot be blank") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull(message = "Username cannot be blank") @NotBlank(message = "Username cannot be blank") String username) {
        this.username = username;
    }

    public @NotNull(message = "Password cannot be blank") @NotBlank(message = "Password cannot be blank") @Size(min = 6, message = "Password cannot be shorter than 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password cannot be blank") @NotBlank(message = "Password cannot be blank") @Size(min = 6, message = "Password cannot be shorter than 6 characters")String password) {
        this.password = password;
    }
}
