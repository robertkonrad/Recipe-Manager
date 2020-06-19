package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.entity.User;

public interface UserService {

    void saveUser(User user);

    boolean usernameAvailable(String username);

    boolean emailAvailable(String email);
}
