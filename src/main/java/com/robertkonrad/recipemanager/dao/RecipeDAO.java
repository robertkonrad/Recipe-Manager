package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;

import java.util.List;

public interface RecipeDAO {

    List<Recipe> getAllRecipes();

    Recipe getRecipe(int recipeId);
}
