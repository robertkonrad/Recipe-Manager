package com.robertkonrad.recipemanager.entity;

import com.robertkonrad.recipemanager.validation.UserMatchesPassword;
import com.robertkonrad.recipemanager.validation.UserPassword;
import com.robertkonrad.recipemanager.validation.UserUniqueEmail;
import com.robertkonrad.recipemanager.validation.UserUniqueUsername;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@UserMatchesPassword(groups = {Group1.class})
@Entity
@Table(name = "users", schema = "recipemanager")
public class User {

    @UserUniqueUsername(groups = {Group1.class})
    @Id
    @NotNull(groups = {Group1.class})
    @Size(max = 20, message = "Username length must be between 0 and 20 chars.", groups = {Group1.class})
    @NotEmpty(message = "Username cannot be empty.", groups = {Group1.class})
    @Column(name = "username", length = 20)
    private String username;

    @UserPassword(groups = {Group1.class})
    @Size(max = 68, groups = {Group1.class})
    @NotNull(groups = {Group1.class})
    @Column(name = "password", length = 68)
    private String password;

    @Transient
    private String matchingPassword;

    @NotNull
    @Column(name = "enabled", columnDefinition = "integer default 1")
    private int enabled = 1;

    @UserUniqueEmail(groups = {Group1.class})
    @NotNull(groups = {Group1.class})
    @NotEmpty(message = "Email address cannot be empty.", groups = {Group1.class})
    @Column(name = "email", unique = true, length = 115)
    private String email;

    @OneToOne(mappedBy = "username", cascade = CascadeType.ALL)
    private Role role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "lastModificatedBy", cascade = CascadeType.ALL)
    private List<Recipe> modificatedRecipes;

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
