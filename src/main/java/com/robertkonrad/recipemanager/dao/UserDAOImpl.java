package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public String saveUser(User user) {
        Session session = entityManager.unwrap(Session.class);
        return (String) session.save(user);
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
    public User getUser(String username) {
        Session session = entityManager.unwrap(Session.class);
        try {
            return session.createQuery("FROM User WHERE username='" + username + "'", User.class).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
