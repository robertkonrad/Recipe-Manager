package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;

import java.util.List;

public interface RecipeDAO {

    List<Recipe> getAllRecipes();

    Recipe getRecipe(int recipeId);

    int saveRecipe(Recipe recipe);

    List<Recipe> getRecipesByPage(int page, int recipesOnOnePage);

    void deleteRecipe(Recipe recipe);

    void updateRecipe(Recipe recipe);

    List<Recipe> getRecipesByPageAndSearch(int page, int recipesOnOnePage, String q);

    int getNumberOfAllSearchedRecipes(String q);

    List<Recipe> getUserRecipesByPage(int page, int recipesOnOnePage, String name);

    List<Recipe> getAllUserRecipes(String name);

    List<Recipe> getUserRecipesByPageAndSearch(int page, int recipesOnOnePage, String q, String name);

    int getNumberOfAllSearchedUserRecipes(String q, String name);

    Boolean isFavourite(int recipeId, String name);

    void changeFavouriteRecipeStatus(int recipeId, String name);

    List<Recipe> getUserFavouriteRecipesByPage(int page, int recipesOnOnePage, String name);

    List<Recipe> getAllUserFavouriteRecipes(String name);

    List<Recipe> getUserFavouriteRecipesByPageAndSearch(int page, int recipesOnOnePage, String q, String name);

    int getNumberOfAllSearchedUserFavouriteRecipes(String q, String name);

    List<Recipe> getAdvancedSearchRecipesByPageAndSearch(int page, int recipesOnOnePage, List<String> ingredients);

    int getNumberOfAllAdvancedSearchedRecipes(List<String> ingredients);
}
