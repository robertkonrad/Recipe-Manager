package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Role;
import com.robertkonrad.recipemanager.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
        Session session = entityManager.unwrap(Session.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setUser(user);
        session.save(user);
        session.save(role);
    }

    @Override
    public boolean usernameAvailable(String username) {
        Session session = entityManager.unwrap(Session.class);
        User user = session.get(User.class, username);
        if (user == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean emailAvailable(String email) {
        Session session = entityManager.unwrap(Session.class);
        try {
            User user = session.createQuery("FROM User WHERE email='" + email + "'", User.class).getSingleResult();
            return false;
        } catch (NoResultException e) {
            return true;
        }
    }

    @Override
    public List<User> getUsers() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public String getUserRole(String username) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("SELECT authority FROM Role WHERE username='" + username + "'", String.class).getSingleResult();
    }

    @Override
    public User getUser(String username) {
        Session session = entityManager.unwrap(Session.class);
        try {
            User user = session.createQuery("FROM User WHERE username='" + username + "'", User.class).getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null;
        }
    }
}
