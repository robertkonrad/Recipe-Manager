package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", schema = "recipemanager")
public class User {

    @Id
    @NotNull
    @Size(max = 20, message = "Username length must be between 0 and 20 chars.")
    @NotEmpty(message = "Username cannot be empty.")
    @Column(name = "username", length = 20)
    private String username;

    @Size(max = 68)
    @NotNull
    @Column(name = "password", length = 68)
    private String password;

    @Transient
    private String matchingPassword;

    @NotNull
    @Column(name = "enabled", columnDefinition = "integer default 1")
    private int enabled = 1;

    @Column(name = "email", unique = true, length = 115)
    private String email;

    @OneToOne(mappedBy = "username", cascade = CascadeType.ALL)
    private Role role;

    public User() {

    }

    public User(@NotNull @Size(max = 20, message = "Username length must be between 0 and 20 chars.") @NotEmpty(message = "Username cannot be empty.") String username, @Size(max = 68) @NotNull String password, String matchingPassword, @NotNull int enabled, String email, Role role) {
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.enabled = enabled;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
