package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.entity.Recipe;

import java.util.List;

public interface RecipeService {

    List<Recipe> getAllRecipes();

    Recipe getRecipe(int recipeId);
}
