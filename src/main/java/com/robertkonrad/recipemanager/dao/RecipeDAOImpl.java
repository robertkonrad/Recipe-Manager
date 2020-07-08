package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.RecipeIngredient;
import com.robertkonrad.recipemanager.entity.Review;
import com.robertkonrad.recipemanager.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

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
        int newId = (int)session.save(recipe);
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
}
