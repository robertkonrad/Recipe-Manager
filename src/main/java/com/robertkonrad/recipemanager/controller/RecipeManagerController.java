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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class RecipeManagerController {

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value = "/")
    public String index() {
        return "redirect:/page/1";
    }

    @RequestMapping(value = "/page/{page}")
    public String indexPage(@PathVariable int page, Model theModel) {
        int recipesOnOnePage = 12, pages;
        List<Recipe> recipes = recipeService.getRecipesByPage(page, recipesOnOnePage);
        pages = (int) Math.ceil((double) recipeService.getAllRecipes().size() / recipesOnOnePage);
        if (pages == 0) {
            pages++;
        }
        theModel.addAttribute("recipes", recipes);
        theModel.addAttribute("pages", pages);
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
    public String saveRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult theBindingResult, @RequestParam("file") MultipartFile file,
                             @RequestParam("ingredient-li") String[] ingredients, @RequestParam("amount-li") double[] amount, @RequestParam("unit-li") String[] unit) {
        if (theBindingResult.hasErrors()) {
            return "recipe-form";
        } else {
            List<String[]> ingredientsList = new ArrayList<>();
            for (int i = 0; i < ingredients.length; i++) {
                String[] temp = new String[3];
                temp[0] = ingredients[i];
                temp[1] = String.valueOf(amount[i]);
                temp[2] = unit[i];
                ingredientsList.add(temp);
            }
            recipeService.saveRecipe(recipe, file, ingredientsList);
            return "redirect:/";
        }
    }
}
