package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.dao.RecipeIngredientDAO;
import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.RecipeIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RecipeIngredientServiceImpl implements RecipeIngredientService {

    @Autowired
    private RecipeIngredientDAO recipeIngredientDAO;

    @Override
    @Transactional
    public void saveOrUpdateIngredients(List<String[]> ingredientsList, Recipe recipe) {
        recipeIngredientDAO.saveOrUpdateIngredients(ingredientsList, recipe);
    }

    @Override
    @Transactional
    public void deleteIngredients(List<RecipeIngredient> ingredients) {
        recipeIngredientDAO.deleteIngredients(ingredients);
    }

    @Override
    @Transactional
    public List<RecipeIngredient> getIngredients(int recipeId) {
        return recipeIngredientDAO.getIngredients(recipeId);
    }
}
