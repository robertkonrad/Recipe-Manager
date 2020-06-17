package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.entity.User;

public interface UserService {

    public void saveUser(User user);

    public boolean usernameAvailable(String username);

    public boolean emailAvailable(String email);
}
