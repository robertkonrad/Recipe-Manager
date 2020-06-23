package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Repository
public class RecipeDAOImpl implements RecipeDAO{

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
    public void saveRecipe(Recipe recipe, MultipartFile file) {
        Session session = entityManager.unwrap(Session.class);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = session.get(User.class, auth.getName());
        recipe.setAuthor(user);
        Date date = new Date();
        recipe.setCreatedDate(date);
        recipe.setLastModificated(date);
        if (!file.isEmpty()){
            String folder = "src/main/resources/static/img/";
            System.out.println(folder);
            Path path = Paths.get(folder + recipe.getTitle() + "-" + file.getOriginalFilename());
            System.out.println(path);
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
        session.save(recipe);
    }
}
