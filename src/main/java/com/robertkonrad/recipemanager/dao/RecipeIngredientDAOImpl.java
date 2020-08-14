package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.RecipeIngredient;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RecipeIngredientDAOImpl implements RecipeIngredientDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void saveOrUpdateIngredients(List<String[]> ingredientsList, Recipe recipe) {
        Session session = entityManager.unwrap(Session.class);
        for (String[] ingredient : ingredientsList) {
            if (!ingredient[0].trim().isEmpty()) {
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setIngredientName(ingredient[0]);
                recipeIngredient.setAmount(Double.parseDouble(ingredient[1]));
                recipeIngredient.setUnit(ingredient[2]);
                recipeIngredient.setRecipe(recipe);
                session.merge(recipeIngredient);
            }
        }
    }

    @Override
    public void deleteIngredients(List<RecipeIngredient> ingredients) {
        Session session = entityManager.unwrap(Session.class);
        for (RecipeIngredient ingredient : ingredients) {
            session.delete(ingredient);
        }
    }

    @Override
    public List<RecipeIngredient> getIngredients(int recipeId) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM RecipeIngredient ri WHERE ri.recipe.id ='"+ recipeId +"'", RecipeIngredient.class).getResultList();
    }
}
