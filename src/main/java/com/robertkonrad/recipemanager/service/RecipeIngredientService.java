package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.RecipeIngredient;

import java.util.List;

public interface RecipeIngredientService {

    void saveOrUpdateIngredients(List<String[]> ingredientsList, Recipe recipe);

    void deleteIngredients(List<RecipeIngredient> ingredients);

    List<RecipeIngredient> getIngredients(int recipeId);
}
