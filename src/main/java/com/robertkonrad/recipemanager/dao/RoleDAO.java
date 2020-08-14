package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Role;

public interface RoleDAO {

    void saveRole(Role role);

    String getUserRole(String username);
}
