package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);

    boolean usernameAvailable(String username);

    boolean emailAvailable(String email);

    List<User> getUsers();

    String getUserRole(String username);

    User getUser(String username);
}
