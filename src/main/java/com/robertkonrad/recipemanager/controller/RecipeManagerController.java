package com.robertkonrad.recipemanager.controller;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

    @RequestMapping(value = "/recipe/add")
    public String addRecipe(Model theModel) {
        Recipe recipe = new Recipe();
        theModel.addAttribute("recipe", recipe);
        return "recipe-form";
    }

    @RequestMapping(value = "/recipe/save")
    public String saveRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult theBindingResult, @RequestParam("file") MultipartFile file) {
        if (theBindingResult.hasErrors()){
            return "recipe-form";
        } else {
            recipeService.saveRecipe(recipe, file);
            return "redirect:/";
        }
    }
}
