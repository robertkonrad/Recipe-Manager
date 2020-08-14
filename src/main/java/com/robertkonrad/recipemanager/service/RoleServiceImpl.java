package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.dao.RoleDAO;
import com.robertkonrad.recipemanager.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleDAO.saveRole(role);
    }

    @Transactional
    @Override
    public String getUserRole(String username) {
        return roleDAO.getUserRole(username);
    }
}

