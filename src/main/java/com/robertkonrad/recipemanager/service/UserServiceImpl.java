package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.dao.UserDAO;
import com.robertkonrad.recipemanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public void saveUser(User user) {
        userDAO.saveUser(user);
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
}
