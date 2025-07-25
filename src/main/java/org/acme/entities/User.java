package org.acme.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
@UserDefinition
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Username
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 6)
    @Password
    @Column(name = "password_hash")
    private String password;

    @Column(name = "alumni_id")
    private int alumniId;

    @Roles
    @Column(name = "roles")
    public String role;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 6) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 6) String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(int alumniId) {
        this.alumniId = alumniId;
    }
}
