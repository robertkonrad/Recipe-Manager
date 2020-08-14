package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.User;

import java.util.List;

public interface UserDAO {

    String saveUser(User user);

    boolean usernameAvailable(String username);

    boolean emailAvailable(String email);

    List<User> getUsers();

    User getUser(String username);
}
