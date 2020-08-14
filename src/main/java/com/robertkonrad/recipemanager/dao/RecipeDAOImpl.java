package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.FavouriteRecipe;
import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import java.util.*;
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
    public int saveRecipe(Recipe recipe) {
        Session session = entityManager.unwrap(Session.class);
        return (int) session.save(recipe);
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
        return session.createQuery("FROM Recipe r ORDER BY r.createdDate DESC", Recipe.class)
                .setFirstResult(minRowNum).setMaxResults(recipesOnOnePage)
                .getResultList();
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        Session session = entityManager.unwrap(Session.class);
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
            results = recipes.stream().sorted(Comparator.comparing(Recipe::getCreatedDate, Comparator.reverseOrder())).skip(minRowNum).limit(recipesOnOnePage).collect(Collectors.toList());
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
        return session.createQuery("FROM Recipe r WHERE r.author.username = '" + name + "' ORDER BY r.createdDate DESC", Recipe.class)
                .setFirstResult(minRowNum).setMaxResults(recipesOnOnePage)
                .getResultList();
    }

    @Override
    public List<Recipe> getAllUserRecipes(String name) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Recipe r WHERE r.author.username = '" + name + "' ORDER BY r.createdDate DESC", Recipe.class).getResultList();
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
        }
        results = recipes.stream().sorted(Comparator.comparing(Recipe::getCreatedDate, Comparator.reverseOrder())).skip(minRowNum).limit(recipesOnOnePage).collect(Collectors.toList());
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
            Date date = new Date();
            newFavouriteRecipe.setAddToFavouriteDate(date);
            newFavouriteRecipe.setRecipe(recipe);
            newFavouriteRecipe.setUser(user);
            session.save(newFavouriteRecipe);
        } else {
            session.delete(favouriteRecipe);
        }
    }

    @Override
    public List<Recipe> getUserFavouriteRecipesByPage(int page, int recipesOnOnePage, String name) {
        Session session = entityManager.unwrap(Session.class);
        int minRowNum;
        List<Recipe> recipes = new ArrayList<>();
        if (page == 1) {
            minRowNum = 0;
        } else {
            minRowNum = (page - 1) * recipesOnOnePage;
        }
        List<Integer> recipesQueryList = session.createQuery("SELECT r.id FROM Recipe r INNER JOIN r.favouriteRecipeList frl on r.id = frl.recipe.id WHERE frl.user.username = '" + name + "' ORDER BY frl.addToFavouriteDate DESC")
                .setFirstResult(minRowNum).setMaxResults(recipesOnOnePage)
                .list();
        for (int id : recipesQueryList) {
            Recipe recipeQuery = session.get(Recipe.class, id);
            recipes.add(recipeQuery);
        }
        return recipes;
    }

    @Override
    public List<Recipe> getAllUserFavouriteRecipes(String name) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> recipes = new ArrayList<>();
        List<Integer> recipesQueryList = session.createQuery("SELECT r.id FROM Recipe r INNER JOIN r.favouriteRecipeList frl on r.id = frl.recipe.id WHERE frl.user.username = '" + name + "' ORDER BY frl.addToFavouriteDate DESC").list();
        for (int id : recipesQueryList) {
            Recipe recipeQuery = session.get(Recipe.class, id);
            recipes.add(recipeQuery);
        }
        return recipes;
    }

    @Override
    public List<Recipe> getUserFavouriteRecipesByPageAndSearch(int page, int recipesOnOnePage, String q, String name) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> results;
        int minRowNum;
        if (page == 1) {
            minRowNum = 0;
        } else {
            minRowNum = (page - 1) * recipesOnOnePage;
        }
        String[] splitQ = q.split(" ");
        for (String sq : splitQ) {
            List<Integer> recipesQueryList = session.createQuery("SELECT r.id FROM Recipe r INNER JOIN r.favouriteRecipeList frl on r.id = frl.recipe.id WHERE lower(r.title) like lower(concat('%','" + sq + "','%')) and frl.user.username = '" + name + "'" +
                    "or lower(r.directions) like lower(concat('%','" + sq + "','%')) and frl.user.username = '" + name + "'").list();
            List<Integer> recipesQuery2List = session.createQuery(
                    "SELECT r.id FROM Recipe r INNER JOIN r.ingredient i " +
                            "on r.id = i.recipe.id WHERE lower(i.ingredientName) like lower(concat('%','" + sq + "','%')) and r.author.username = '" + name + "'").list();
            List<Recipe> recipesQuery = new ArrayList<>();
            List<Recipe> recipesQuery2 = new ArrayList<>();
            for (int id : recipesQueryList) {
                Recipe recipeQuery = session.get(Recipe.class, id);
                recipesQuery.add(recipeQuery);
            }
            for (int id : recipesQuery2List) {
                try {
                    FavouriteRecipe fr = session.createQuery("FROM FavouriteRecipe fr WHERE fr.recipe.id='" + id + "' and fr.user.username='" + name + "'", FavouriteRecipe.class).getSingleResult();
                    Recipe recipeQuery2 = session.get(Recipe.class, id);
                    recipesQuery2.add(recipeQuery2);
                } catch (NoResultException e) {

                }
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
        results = recipes.stream().skip(minRowNum).limit(recipesOnOnePage).collect(Collectors.toList());
        return results;
    }

    @Override
    public int getNumberOfAllSearchedUserFavouriteRecipes(String q, String name) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> recipes = new ArrayList<>();
        String[] splitQ = q.split(" ");
        for (String sq : splitQ) {
            List<Integer> recipesQueryList = session.createQuery("SELECT r.id FROM Recipe r INNER JOIN r.favouriteRecipeList frl on r.id = frl.recipe.id WHERE lower(r.title) like lower(concat('%','" + sq + "','%')) and frl.user.username = '" + name + "'" +
                    "or lower(r.directions) like lower(concat('%','" + sq + "','%')) and frl.user.username = '" + name + "'").list();
            List<Integer> recipesQuery2List = session.createQuery(
                    "SELECT r.id FROM Recipe r INNER JOIN r.ingredient i " +
                            "on r.id = i.recipe.id WHERE lower(i.ingredientName) like lower(concat('%','" + sq + "','%')) and r.author.username = '" + name + "'").list();
            List<Recipe> recipesQuery = new ArrayList<>();
            List<Recipe> recipesQuery2 = new ArrayList<>();
            for (int id : recipesQueryList) {
                Recipe recipeQuery = session.get(Recipe.class, id);
                recipesQuery.add(recipeQuery);
            }
            for (int id : recipesQuery2List) {
                try {
                    FavouriteRecipe fr = session.createQuery("FROM FavouriteRecipe fr WHERE fr.recipe.id='" + id + "' and fr.user.username='" + name + "'", FavouriteRecipe.class).getSingleResult();
                    Recipe recipeQuery2 = session.get(Recipe.class, id);
                    recipesQuery2.add(recipeQuery2);
                } catch (NoResultException e) {

                }
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
    public List<Recipe> getAdvancedSearchRecipesByPageAndSearch(int page, int recipesOnOnePage, List<String> ingredients) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> results;
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> recipesTemp = new ArrayList<>();
        Set<Integer> recipesId = new HashSet<>();
        int minRowNum;
        if (page == 1) {
            minRowNum = 0;
        } else {
            minRowNum = (page - 1) * recipesOnOnePage;
        }
        for (String ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                if (recipesId.isEmpty()) {
                    recipesId.addAll(session.createQuery("SELECT ri.recipe.id FROM RecipeIngredient ri WHERE lower(ri.ingredientName) like lower(concat('%','" + ingredient + "','%'))").list());
                } else {
                    recipesTemp = session.createQuery("SELECT ri.recipe.id FROM RecipeIngredient ri WHERE lower(ri.ingredientName) like lower(concat('%','" + ingredient + "','%'))").list();
                    recipesId.retainAll(recipesTemp);
                }
            }
        }
        for (int id : recipesId) {
            recipes.add(session.get(Recipe.class, id));
        }
        results = recipes.stream().skip(minRowNum).limit(recipesOnOnePage).collect(Collectors.toList());
        return results;
    }

    @Override
    public int getNumberOfAllAdvancedSearchedRecipes(List<String> ingredients) {
        Session session = entityManager.unwrap(Session.class);
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> recipesTemp = new ArrayList<>();
        Set<Integer> recipesId = new HashSet<>();
        for (String ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                if (recipesId.isEmpty()) {
                    recipesId.addAll(session.createQuery("SELECT ri.recipe.id FROM RecipeIngredient ri WHERE lower(ri.ingredientName) like lower(concat('%','" + ingredient + "','%'))").list());
                } else {
                    recipesTemp = session.createQuery("SELECT ri.recipe.id FROM RecipeIngredient ri WHERE lower(ri.ingredientName) like lower(concat('%','" + ingredient + "','%'))").list();
                    recipesId.retainAll(recipesTemp);
                }
            }
        }
        for (int id : recipesId) {
            recipes.add(session.get(Recipe.class, id));
        }
        return recipes.size();
    }
}
