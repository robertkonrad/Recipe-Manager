package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.User;

public interface UserDAO {

    void saveUser(User user);

    boolean usernameAvailable(String username);

    boolean emailAvailable(String email);
}
