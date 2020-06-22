package com.robertkonrad.recipemanager.controller;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class RecipeManagerController {

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value = "/")
    public String index(Model theModel) {
        List<Recipe> recipes = recipeService.getAllRecipes();
        theModel.addAttribute("recipes", recipes);
        return "index";
    }

    @RequestMapping(value = "/recipe/{recipeId}")
    public String recipeDetails(@PathVariable int recipeId, Model theModel) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        theModel.addAttribute("recipe", recipe);
        return "recipe-details";
    }
}
