package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Role;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class RoleDAOImpl implements RoleDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void saveRole(Role role) {
        Session session = entityManager.unwrap(Session.class);
        session.save(role);
    }

    @Override
    public String getUserRole(String username) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("SELECT authority FROM Role WHERE username='" + username + "'", String.class).getSingleResult();
    }
}
