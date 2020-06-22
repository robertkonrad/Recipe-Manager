package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RecipeDAOImpl implements RecipeDAO{

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Recipe> getAllRecipes() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Recipe", Recipe.class).getResultList();
    }

    @Override
    public Recipe getRecipe(int recipeId) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Recipe.class, recipeId);
    }
}
