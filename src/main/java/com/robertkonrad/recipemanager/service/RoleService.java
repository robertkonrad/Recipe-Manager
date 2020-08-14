package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.entity.Role;

public interface RoleService {

    void saveRole(Role role);

    String getUserRole(String username);
}
