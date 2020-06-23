package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeDAO {

    List<Recipe> getAllRecipes();

    Recipe getRecipe(int recipeId);

    void saveRecipe(Recipe recipe, MultipartFile file);
}
