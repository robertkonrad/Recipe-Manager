package com.robertkonrad.recipemanager.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ReviewDAOImpl implements ReviewDAO {

    @Autowired
    EntityManager entityManager;
}
