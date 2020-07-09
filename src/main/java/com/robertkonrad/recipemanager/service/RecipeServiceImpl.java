package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.dao.RecipeDAO;
import com.robertkonrad.recipemanager.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService{

    @Autowired
    private RecipeDAO recipeDAO;

    @Override
    @Transactional
    public List<Recipe> getAllRecipes() {
        return recipeDAO.getAllRecipes();
    }

    @Override
    @Transactional
    public Recipe getRecipe(int recipeId) {
        return recipeDAO.getRecipe(recipeId);
    }

    @Override
    @Transactional
    public void saveRecipe(Recipe recipe, MultipartFile file, List<String[]> ingredientsList) {
        recipeDAO.saveRecipe(recipe, file, ingredientsList);
    }

    @Override
    @Transactional
    public List<Recipe> getRecipesByPage(int page, int recipesOnOnePage) {
        return recipeDAO.getRecipesByPage(page, recipesOnOnePage);
    }

    @Override
    @Transactional
    public void deleteRecipe(int recipeId) {
        recipeDAO.deleteRecipe(recipeId);
    }

    @Override
    @Transactional
    public void updateRecipe(Recipe recipe, MultipartFile file, List<String[]> ingredientsList) {
        recipeDAO.updateRecipe(recipe, file, ingredientsList);
    }

    @Override
    @Transactional
    public List<Recipe> getRecipesByPageAndSearch(int page, int recipesOnOnePage, String q) {
        return recipeDAO.getRecipesByPageAndSearch(page, recipesOnOnePage, q);
    }

    @Override
    @Transactional
    public int getNumberOfAllSearchedRecipes(String q) {
        return recipeDAO.getNumberOfAllSearchedRecipes(q);
    }
}
