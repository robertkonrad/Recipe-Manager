package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RecipeIngredientDAOTest {

    @Autowired
    private RecipeIngredientDAO recipeIngredientDAO;

    @Autowired
    private RecipeDAO recipeDAO;

    @Transactional
    @Test
    @Rollback
    public void saveOrUpdateIngredientsTest() {
        Recipe recipe = new Recipe();
        recipe.setTitle("test");
        recipeDAO.saveRecipe(recipe);
        List<String[]> recipeIngredientList = new ArrayList<>();
        String[] recipeIngredientArray = new String[3];
        String[] recipeIngredientArray2 = new String[3];
        String[] recipeIngredientArray3 = new String[3];
        recipeIngredientArray[0] = "ri";
        recipeIngredientArray[1] = "1";
        recipeIngredientArray[2] = "kg";
        recipeIngredientArray2[0] = "ri";
        recipeIngredientArray2[1] = "1";
        recipeIngredientArray2[2] = "kg";
        recipeIngredientArray3[0] = "ri";
        recipeIngredientArray3[1] = "1";
        recipeIngredientArray3[2] = "kg";
        recipeIngredientList.add(recipeIngredientArray);
        recipeIngredientList.add(recipeIngredientArray2);
        recipeIngredientList.add(recipeIngredientArray3);
        recipeIngredientDAO.saveOrUpdateIngredients(recipeIngredientList, recipe);
        Assert.assertEquals(3, recipeIngredientDAO.getIngredients(recipe.getId()).size());
    }

    @Transactional
    @Test
    @Rollback
    public void deleteIngredients() {
        Recipe recipe = new Recipe();
        recipe.setTitle("test");
        recipeDAO.saveRecipe(recipe);
        List<String[]> recipeIngredientList = new ArrayList<>();
        String[] recipeIngredientArray = new String[3];
        String[] recipeIngredientArray2 = new String[3];
        String[] recipeIngredientArray3 = new String[3];
        recipeIngredientArray[0] = "ri";
        recipeIngredientArray[1] = "1";
        recipeIngredientArray[2] = "kg";
        recipeIngredientArray2[0] = "ri";
        recipeIngredientArray2[1] = "1";
        recipeIngredientArray2[2] = "kg";
        recipeIngredientArray3[0] = "ri";
        recipeIngredientArray3[1] = "1";
        recipeIngredientArray3[2] = "kg";
        recipeIngredientList.add(recipeIngredientArray);
        recipeIngredientList.add(recipeIngredientArray2);
        recipeIngredientList.add(recipeIngredientArray3);
        recipeIngredientDAO.saveOrUpdateIngredients(recipeIngredientList, recipe);
        Assert.assertEquals(3, recipeIngredientDAO.getIngredients(recipe.getId()).size());
        recipeIngredientDAO.deleteIngredients(recipeIngredientDAO.getIngredients(recipe.getId()));
        Assert.assertEquals(0, recipeIngredientDAO.getIngredients(recipe.getId()).size());
    }

    @Transactional
    @Test
    @Rollback
    public void getIngredients() {
        Recipe recipe = new Recipe();
        recipe.setTitle("test");
        recipeDAO.saveRecipe(recipe);
        List<String[]> recipeIngredientList = new ArrayList<>();
        String[] recipeIngredientArray = new String[3];
        recipeIngredientArray[0] = "ri";
        recipeIngredientArray[1] = "1";
        recipeIngredientArray[2] = "kg";
        recipeIngredientList.add(recipeIngredientArray);
        Assert.assertEquals(0, recipeIngredientDAO.getIngredients(recipe.getId()).size());
        recipeIngredientDAO.saveOrUpdateIngredients(recipeIngredientList, recipe);
        Assert.assertEquals(1, recipeIngredientDAO.getIngredients(recipe.getId()).size());
        Assert.assertEquals("ri", recipeIngredientDAO.getIngredients(recipe.getId()).get(0).getIngredientName());
    }
}
