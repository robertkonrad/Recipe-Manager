package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RecipeDAOImpl implements RecipeDAO {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    ServletContext context;

    @Override
    public List<Recipe> getAllRecipes() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Recipe", Recipe.class).getResultList();
    }

    @Override
    public Recipe getRecipe(int recipeId) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Recipe.class, recipeId);
    }

    @Override
    public void saveRecipe(Recipe recipe, MultipartFile file, List<String[]> ingredientsList) {
        Session session = entityManager.unwrap(Session.class);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = session.get(User.class, auth.getName());
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            recipe.setImage(recipe.getTitle() + "-" + file.getOriginalFilename());
        } else {
            recipe.setImage("");
        }
        int newId = (int) session.save(recipe);
        Recipe newRecipe = session.get(Recipe.class, newId);
        if (!ingredientsList.isEmpty()) {
            for (String[] ingredient : ingredientsList) {
                if (!ingredient[0].trim().isEmpty()) {
                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setIngredientName(ingredient[0]);
                    recipeIngredient.setAmount(Double.parseDouble(ingredient[1]));
                    recipeIngredient.setUnit(ingredient[2]);
                    recipeIngredient.setRecipe(newRecipe);
                    session.merge(recipeIngredient);
                }
            }
        }
    }

    @Override
    public List<Recipe> getRecipesByPage(int page, int recipesOnOnePage) {
        Session session = entityManager.unwrap(Session.class);
        int minRowNum;
        if (page == 1) {
            minRowNum = 0;
        } else {
            minRowNum = (page - 1) * recipesOnOnePage;
        }
        return session.createQuery("FROM Recipe", Recipe.class)
                .setFirstResult(minRowNum).setMaxResults(recipesOnOnePage)
                .getResultList();
    }

    @Override
    public void deleteRecipe(int recipeId) {
        Session session = entityManager.unwrap(Session.class);
        List<Review> reviews = session.createQuery("FROM Review r WHERE r.recipe.id='" + recipeId + "'", Review.class).getResultList();
        List<RecipeIngredient> ingredients = session.createQuery("FROM RecipeIngredient i WHERE i.recipe.id='" + recipeId + "'", RecipeIngredient.class).getResultList();
        Recipe recipe = session.get(Recipe.class, recipeId);
        for (Review review : reviews) {
            session.delete(review);
        }
        for (RecipeIngredient ingredient : ingredients) {
            session.delete(ingredient);
        }
        if (!recipe.getImage().equals("")) {
            String folder = "src/main/resources/static/img/";
            File file = new File(folder + recipe.getImage());
            file.delete();
        }
        session.delete(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe, MultipartFile file, List<String[]> ingredientsList) {
        Session session = entityManager.unwrap(Session.class);
        Recipe oldRecipe = session.get(Recipe.class, recipe.getId());
        recipe.setCreatedDate(oldRecipe.getCreatedDate());
        Date date = new Date();
        recipe.setLastModificated(date);
        recipe.setAuthor(oldRecipe.getAuthor());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = session.get(User.class, auth.getName());
        recipe.setLastModificatedBy(user);
        if (!file.isEmpty()) {
            String folder = "src/main/resources/static/img/";
            Path path = Paths.get(folder + recipe.getTitle() + "-" + file.getOriginalFilename());
            try {
                file.transferTo(path);
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            recipe.setImage(recipe.getTitle() + "-" + file.getOriginalFilename());
        } else {
            recipe.setImage(oldRecipe.getImage());
        }
        List<RecipeIngredient> ingredients = session.createQuery("FROM RecipeIngredient i WHERE i.recipe.id='" + recipe.getId() + "'", RecipeIngredient.class)
                .getResultList();
        for (RecipeIngredient ingredient : ingredients) {
            session.delete(ingredient);
        }
        if (!ingredientsList.isEmpty()) {
            for (String[] ingre : ingredientsList) {
                if (!ingre[0].trim().isEmpty()) {
                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setIngredientName(ingre[0]);
                    recipeIngredient.setAmount(Double.parseDouble(ingre[1]));
                    recipeIngredient.setUnit(ingre[2]);
                    recipeIngredient.setRecipe(recipe);
                    session.merge(recipeIngredient);
                }
            }
        }
        session.merge(recipe);
    }

    @Override
    public List<Recipe> getRecipesByPageAndSearch(int page, int recipesOnOnePage, String q) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> results = new ArrayList<>();
        int minRowNum;
        if (page == 1) {
            minRowNum = 0;
        } else {
            minRowNum = (page - 1) * recipesOnOnePage;
        }
        String[] splitQ = q.split(" ");
        for (String sq : splitQ) {
            List<Recipe> recipesQuery = session.createQuery("FROM Recipe r WHERE lower(r.title) like lower(concat('%','" + sq + "','%')) " +
                    "or lower(r.directions) like lower(concat('%','" + sq + "','%'))", Recipe.class).getResultList();
            List<Integer> recipesQuery2List = session.createQuery(
                    "SELECT r.id FROM Recipe r INNER JOIN r.ingredient i " +
                            "on r.id = i.recipe.id WHERE lower(i.ingredientName) like lower(concat('%','" + sq + "','%'))").list();
            List<Recipe> recipesQuery2 = new ArrayList<>();
            for (int id : recipesQuery2List) {
                Recipe recipeQuery2 = session.get(Recipe.class, id);
                recipesQuery2.add(recipeQuery2);
            }
            for (Recipe recipe : recipesQuery) {
                if (!recipes.contains(recipe)) {
                    recipes.add(recipe);
                }
            }
            for (Recipe recipe2 : recipesQuery2) {
                if (!recipes.contains(recipe2)) {
                    recipes.add(recipe2);
                }
            }
            results = recipes.stream().skip(minRowNum).limit(recipesOnOnePage).collect(Collectors.toList());
        }
        return results;
    }

    @Override
    public int getNumberOfAllSearchedRecipes(String q) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> recipes = new ArrayList<>();
        String[] splitQ = q.split(" ");
        for (String sq : splitQ) {
            List<Recipe> recipesQuery = session.createQuery("FROM Recipe r WHERE lower(r.title) like lower(concat('%','" + sq + "','%')) " +
                    "or lower(r.directions) like lower(concat('%','" + sq + "','%'))", Recipe.class).getResultList();
            List<Integer> recipesQuery2List = session.createQuery(
                    "SELECT r.id FROM Recipe r INNER JOIN r.ingredient i " +
                            "on r.id = i.recipe.id WHERE lower(i.ingredientName) like lower(concat('%','" + sq + "','%'))").list();
            List<Recipe> recipesQuery2 = new ArrayList<>();
            for (int id : recipesQuery2List) {
                Recipe recipeQuery2 = session.get(Recipe.class, id);
                recipesQuery2.add(recipeQuery2);
            }

            for (Recipe recipe : recipesQuery) {
                if (!recipes.contains(recipe)) {
                    recipes.add(recipe);
                }
            }
            for (Recipe recipe2 : recipesQuery2) {
                if (!recipes.contains(recipe2)) {
                    recipes.add(recipe2);
                }
            }
        }
        return recipes.size();
    }

    @Override
    public List<Recipe> getUserRecipesByPage(int page, int recipesOnOnePage, String name) {
        Session session = entityManager.unwrap(Session.class);
        int minRowNum;
        if (page == 1) {
            minRowNum = 0;
        } else {
            minRowNum = (page - 1) * recipesOnOnePage;
        }
        return session.createQuery("FROM Recipe r WHERE r.author.username = '" + name + "'", Recipe.class)
                .setFirstResult(minRowNum).setMaxResults(recipesOnOnePage)
                .getResultList();
    }

    @Override
    public List<Recipe> getAllUserRecipes(String name) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Recipe r WHERE r.author.username = '" + name + "'", Recipe.class).getResultList();
    }

    @Override
    public List<Recipe> getUserRecipesByPageAndSearch(int page, int recipesOnOnePage, String q, String name) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> results = new ArrayList<>();
        int minRowNum;
        if (page == 1) {
            minRowNum = 0;
        } else {
            minRowNum = (page - 1) * recipesOnOnePage;
        }
        String[] splitQ = q.split(" ");
        for (String sq : splitQ) {
            List<Recipe> recipesQuery = session.createQuery("FROM Recipe r WHERE lower(r.title) like lower(concat('%','" + sq + "','%')) and r.author.username = '" + name + "'" +
                    "or lower(r.directions) like lower(concat('%','" + sq + "','%')) and r.author.username = '" + name + "'", Recipe.class).getResultList();
            List<Integer> recipesQuery2List = session.createQuery(
                    "SELECT r.id FROM Recipe r INNER JOIN r.ingredient i " +
                            "on r.id = i.recipe.id WHERE lower(i.ingredientName) like lower(concat('%','" + sq + "','%')) and r.author.username = '" + name + "'").list();
            List<Recipe> recipesQuery2 = new ArrayList<>();
            for (int id : recipesQuery2List) {
                Recipe recipeQuery2 = session.get(Recipe.class, id);
                recipesQuery2.add(recipeQuery2);
            }
            for (Recipe recipe : recipesQuery) {
                if (!recipes.contains(recipe)) {
                    recipes.add(recipe);
                }
            }
            for (Recipe recipe2 : recipesQuery2) {
                if (!recipes.contains(recipe2)) {
                    recipes.add(recipe2);
                }
            }
            results = recipes.stream().skip(minRowNum).limit(recipesOnOnePage).collect(Collectors.toList());
        }
        return results;
    }

    @Override
    public int getNumberOfAllSearchedUserRecipes(String q, String name) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> recipes = new ArrayList<>();
        String[] splitQ = q.split(" ");
        for (String sq : splitQ) {
            List<Recipe> recipesQuery = session.createQuery("FROM Recipe r WHERE lower(r.title) like lower(concat('%','" + sq + "','%')) and r.author.username = '" + name + "'" +
                    "or lower(r.directions) like lower(concat('%','" + sq + "','%')) and r.author.username = '" + name + "'", Recipe.class).getResultList();
            List<Integer> recipesQuery2List = session.createQuery(
                    "SELECT r.id FROM Recipe r INNER JOIN r.ingredient i " +
                            "on r.id = i.recipe.id WHERE lower(i.ingredientName) like lower(concat('%','" + sq + "','%')) and r.author.username = '" + name + "'").list();
            List<Recipe> recipesQuery2 = new ArrayList<>();
            for (int id : recipesQuery2List) {
                Recipe recipeQuery2 = session.get(Recipe.class, id);
                recipesQuery2.add(recipeQuery2);
            }

            for (Recipe recipe : recipesQuery) {
                if (!recipes.contains(recipe)) {
                    recipes.add(recipe);
                }
            }
            for (Recipe recipe2 : recipesQuery2) {
                if (!recipes.contains(recipe2)) {
                    recipes.add(recipe2);
                }
            }
        }
        return recipes.size();
    }

    @Override
    public Boolean isFavourite(int recipeId, String name) {
        Session session = entityManager.unwrap(Session.class);
        try {
            FavouriteRecipe favouriteRecipe = session.createQuery("FROM FavouriteRecipe fr WHERE fr.user.username = '" + name + "' and fr.recipe.id = '" + recipeId + "'", FavouriteRecipe.class).getSingleResult();
            return true;
        } catch (NoResultException nre) {

        }
        return false;
    }

    @Override
    public void changeFavouriteRecipeStatus(int recipeId, String name) {
        Session session = entityManager.unwrap(Session.class);
        FavouriteRecipe favouriteRecipe = null;
        try {
            favouriteRecipe = session.createQuery("FROM FavouriteRecipe fr WHERE fr.user.username = '" + name + "' and fr.recipe.id = '" + recipeId + "'", FavouriteRecipe.class).getSingleResult();
        } catch (NoResultException nre) {

        }
        if (favouriteRecipe == null) {
            FavouriteRecipe newFavouriteRecipe = new FavouriteRecipe();
            Recipe recipe = session.get(Recipe.class, recipeId);
            User user = session.get(User.class, name);
            newFavouriteRecipe.setRecipe(recipe);
            newFavouriteRecipe.setUser(user);
            session.save(newFavouriteRecipe);
        } else {
            session.delete(favouriteRecipe);
        }
    }
}
