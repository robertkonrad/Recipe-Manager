package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.RecipeIngredient;

import java.util.List;

public interface RecipeIngredientDAO {

    void saveOrUpdateIngredients(List<String[]> ingredientsList, Recipe recipe);

    void deleteIngredients(List<RecipeIngredient> ingredients);

    List<RecipeIngredient> getIngredients(int recipeId);
}
