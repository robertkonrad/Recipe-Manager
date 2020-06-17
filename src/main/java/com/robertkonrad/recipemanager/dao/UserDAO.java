package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.User;

public interface UserDAO {

    public void saveUser(User user);

    public boolean usernameAvailable(String username);

    public boolean emailAvailable(String email);
}
