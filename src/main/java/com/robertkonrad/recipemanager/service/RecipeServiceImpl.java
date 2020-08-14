package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.dao.RecipeDAO;
import com.robertkonrad.recipemanager.dao.RecipeIngredientDAO;
import com.robertkonrad.recipemanager.dao.ReviewDAO;
import com.robertkonrad.recipemanager.dao.UserDAO;
import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.RecipeIngredient;
import com.robertkonrad.recipemanager.entity.Review;
import com.robertkonrad.recipemanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService{

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RecipeIngredientDAO recipeIngredientDAO;

    @Autowired
    private ReviewDAO reviewDAO;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDAO.getUser(auth.getName());
        recipe.setAuthor(user);
        Date date = new Date();
        recipe.setCreatedDate(date);
        recipe.setLastModificated(date);
        if (!file.isEmpty()) {
            String folder = "src/main/resources/static/img/";
            Path path = Paths.get(folder + recipe.getTitle() + "-" + file.getOriginalFilename());
            try {
                file.transferTo(path);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recipe.setImage(recipe.getTitle() + "-" + file.getOriginalFilename());
        } else {
            recipe.setImage("");
        }
        int recipeId = recipeDAO.saveRecipe(recipe);
        Recipe newRecipe = recipeDAO.getRecipe(recipeId);
        if (!ingredientsList.isEmpty()) {
            recipeIngredientDAO.saveOrUpdateIngredients(ingredientsList, newRecipe);
        }
    }

    @Override
    @Transactional
    public List<Recipe> getRecipesByPage(int page, int recipesOnOnePage) {
        return recipeDAO.getRecipesByPage(page, recipesOnOnePage);
    }

    @Override
    @Transactional
    public void deleteRecipe(int recipeId) {
        Recipe recipe = recipeDAO.getRecipe(recipeId);
        List<Review> reviews = recipe.getReviews();
        List<RecipeIngredient> ingredients = recipe.getIngredients();
        reviewDAO.deleteReviews(reviews);
        recipeIngredientDAO.deleteIngredients(ingredients);
        if (!recipe.getImage().equals("")) {
            String folder = "src/main/resources/static/img/";
            File file = new File(folder + recipe.getImage());
            file.delete();
        }
        recipeDAO.deleteRecipe(recipe);
    }

    @Override
    @Transactional
    public void updateRecipe(Recipe recipe, MultipartFile file, List<String[]> ingredientsList) {
        Recipe oldRecipe = recipeDAO.getRecipe(recipe.getId());
        recipe.setCreatedDate(oldRecipe.getCreatedDate());
        Date date = new Date();
        recipe.setLastModificated(date);
        recipe.setAuthor(oldRecipe.getAuthor());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDAO.getUser(auth.getName());
        recipe.setLastModificatedBy(user);
        if (!file.isEmpty()) {
            String folder = "src/main/resources/static/img/";
            Path path = Paths.get(folder + recipe.getTitle() + "-" + file.getOriginalFilename());
            try {
                file.transferTo(path);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recipe.setImage(recipe.getTitle() + "-" + file.getOriginalFilename());
        } else {
            recipe.setImage(oldRecipe.getImage());
        }
        recipeDAO.updateRecipe(recipe);
        List<RecipeIngredient> ingredients = recipeIngredientDAO.getIngredients(recipe.getId());
        recipeIngredientDAO.deleteIngredients(ingredients);
        if (!ingredientsList.isEmpty()) {
            recipeIngredientDAO.saveOrUpdateIngredients(ingredientsList, recipe);
        }
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

    @Override
    @Transactional
    public List<Recipe> getUserRecipesByPage(int page, int recipesOnOnePage, String name) {
        return recipeDAO.getUserRecipesByPage(page, recipesOnOnePage, name);
    }

    @Override
    @Transactional
    public List<Recipe> getAllUserRecipes(String name) {
        return recipeDAO.getAllUserRecipes(name);
    }

    @Override
    @Transactional
    public List<Recipe> getUserRecipesByPageAndSearch(int page, int recipesOnOnePage, String q, String name) {
        return recipeDAO.getUserRecipesByPageAndSearch(page, recipesOnOnePage, q, name);
    }

    @Override
    @Transactional
    public int getNumberOfAllSearchedUserRecipes(String q, String name) {
        return recipeDAO.getNumberOfAllSearchedUserRecipes(q, name);
    }

    @Override
    @Transactional
    public Boolean isFavourite(int recipeId, String name) {
        return recipeDAO.isFavourite(recipeId, name);
    }

    @Override
    @Transactional
    public void changeFavouriteRecipeStatus(int recipeId, String name) {
        recipeDAO.changeFavouriteRecipeStatus(recipeId, name);
    }

    @Override
    @Transactional
    public List<Recipe> getUserFavouriteRecipesByPage(int page, int recipesOnOnePage, String name) {
        return recipeDAO.getUserFavouriteRecipesByPage(page, recipesOnOnePage, name);
    }

    @Override
    @Transactional
    public List<Recipe> getAllUserFavouriteRecipes(String name) {
        return recipeDAO.getAllUserFavouriteRecipes(name);
    }

    @Override
    @Transactional
    public List<Recipe> getUserFavouriteRecipesByPageAndSearch(int page, int recipesOnOnePage, String q, String name) {
        return recipeDAO.getUserFavouriteRecipesByPageAndSearch(page, recipesOnOnePage, q, name);
    }

    @Override
    @Transactional
    public int getNumberOfAllSearchedUserFavouriteRecipes(String q, String name) {
        return recipeDAO.getNumberOfAllSearchedUserFavouriteRecipes(q, name);
    }

    @Override
    @Transactional
    public List<Recipe> getAdvancedSearchRecipesByPageAndSearch(int page, int recipesOnOnePage, List<String> ingredients) {
        return recipeDAO.getAdvancedSearchRecipesByPageAndSearch(page, recipesOnOnePage, ingredients);
    }

    @Override
    @Transactional
    public int getNumberOfAllAdvancedSearchedRecipes(List<String> ingredients) {
        return recipeDAO.getNumberOfAllAdvancedSearchedRecipes(ingredients);
    }
}
