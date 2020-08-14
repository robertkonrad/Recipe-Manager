package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.dao.RoleDAO;
import com.robertkonrad.recipemanager.dao.UserDAO;
import com.robertkonrad.recipemanager.entity.Role;
import com.robertkonrad.recipemanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveUser(User user) {
        Role role = new Role();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String userSaved = userDAO.saveUser(user);
        role.setUser(userDAO.getUser(userSaved));
        roleDAO.saveRole(role);
    }

    @Transactional
    @Override
    public boolean usernameAvailable(String username) {
        return userDAO.usernameAvailable(username);
    }

    @Transactional
    @Override
    public boolean emailAvailable(String email) {
        return userDAO.emailAvailable(email);
    }

    @Transactional
    @Override
    public List<User> getUsers() {
        return userDAO.getUsers();
    }


    @Transactional
    @Override
    public User getUser(String username) {
        return userDAO.getUser(username);
    }
}
