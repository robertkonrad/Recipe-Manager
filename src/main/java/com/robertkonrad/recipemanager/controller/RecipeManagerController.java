package com.robertkonrad.recipemanager.controller;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.Review;
import com.robertkonrad.recipemanager.service.RecipeService;
import com.robertkonrad.recipemanager.service.ReviewService;
import com.robertkonrad.recipemanager.util.PdfUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeManagerController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = "/")
    public String index() {
        return "redirect:/page/1";
    }

    @GetMapping(value = "/page/{page}")
    public String indexPage(@PathVariable int page, Model theModel, @RequestParam(required = false, name = "q") String q) {
        int recipesOnOnePage = 12, pages;
        List<Recipe> recipes;
        if ((q == null) || (StringUtils.isBlank(q))) {
            recipes = recipeService.getRecipesByPage(page, recipesOnOnePage);
            pages = (int) Math.ceil((double) recipeService.getAllRecipes().size() / recipesOnOnePage);
        } else {
            recipes = recipeService.getRecipesByPageAndSearch(page, recipesOnOnePage, q);
            pages = (int) Math.ceil((double) recipeService.getNumberOfAllSearchedRecipes(q) / recipesOnOnePage);
        }
        if (pages == 0) {
            pages++;
        }
        theModel.addAttribute("recipes", recipes);
        theModel.addAttribute("pages", pages);
        theModel.addAttribute("pageTitle", "RecipeManager - Page " + page);
        return "index";
    }

    @GetMapping(value = "/recipe/{recipeId}")
    public String recipeDetails(@PathVariable int recipeId, Model theModel, Authentication authentication) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        List<Review> reviews = recipe.getReviews();
        Review review = new Review();
        if (authentication != null) {
            Boolean isFavourite = recipeService.isFavourite(recipeId, authentication.getName());
            theModel.addAttribute("isFavourite", isFavourite);
        }
        theModel.addAttribute("recipe", recipe);
        theModel.addAttribute("reviews", reviews);
        theModel.addAttribute("review", review);
        theModel.addAttribute("pageTitle", "RecipeManager - " + recipe.getTitle());
        return "recipe-details";
    }

    @GetMapping(value = "/recipe/add")
    public String addRecipe(Model theModel) {
        Recipe recipe = new Recipe();
        theModel.addAttribute("recipe", recipe);
        theModel.addAttribute("pageTitle", "RecipeManager - Add new recipe ");
        return "recipe-form";
    }

    @PostMapping(value = "/recipe/save")
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

    @PostMapping(value = "/recipe/{recipeId}/delete")
    public String deleteRecipe(@PathVariable int recipeId) {
        recipeService.deleteRecipe(recipeId);
        return "redirect:/";
    }

    @PostMapping(value = "/recipe/{recipeId}/edit")
    public String updateRecipe(@PathVariable int recipeId, Model theModel) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        theModel.addAttribute("recipe", recipe);
        theModel.addAttribute("pageTitle", "RecipeManager - Update recipe " + recipe.getTitle());
        return "recipe-update-form";
    }

    @PostMapping(value = "/recipe/{recipeId}/edit/save")
    public Object saveUpdateRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,
                                   BindingResult theBindingResult, @RequestParam(value = "ingredient-li", required = false) String[] ingredients,
                                   @RequestParam(value = "amount-li", required = false) double[] amount, @RequestParam(value = "unit-li", required = false) String[] unit,
                                   @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (theBindingResult.hasErrors()) {
            return "recipe-update-form";
        } else {
            List<String[]> ingredientsList = new ArrayList<>();
            if (ingredients != null) {
                for (int i = 0; i < ingredients.length; i++) {
                    String[] temp = new String[3];
                    temp[0] = ingredients[i];
                    temp[1] = String.valueOf(amount[i]);
                    temp[2] = unit[i];
                    ingredientsList.add(temp);
                }
            }
            redirectAttributes.addFlashAttribute("redirected", "redirected");
            recipeService.updateRecipe(recipe, file, ingredientsList);
            return "redirect:/recipe/{recipeId}";
        }
    }

    @PostMapping(value = "/recipe/{recipeId}/review/add")
    public String saveReview(@Valid @ModelAttribute("review") Review review, BindingResult theBindingResult, @PathVariable int recipeId, Model theModel) {
        if (theBindingResult.hasErrors()) {
            Recipe recipe = recipeService.getRecipe(recipeId);
            List<Review> reviews = recipe.getReviews();
            theModel.addAttribute("recipe", recipe);
            theModel.addAttribute("reviews", reviews);
            theModel.addAttribute("review", review);
            return "recipe-details";
        } else {
            reviewService.saveReview(recipeId, review);
            return "redirect:/recipe/{recipeId}";
        }
    }

    @PostMapping(value = "/recipe/{recipeId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf(@PathVariable int recipeId) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        ByteArrayInputStream byteArrayInputStream = PdfUtil.generatePdf(recipe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline; filename=" + recipe.getTitle() + ".pdf");
        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));
    }

    @GetMapping(value = "my-recipes/page/{page}")
    public String myRecipe(@PathVariable int page, Model theModel, @RequestParam(required = false, name = "q") String q, Authentication authentication) {
        int recipesOnOnePage = 12, pages;
        List<Recipe> recipes;
        if ((q == null) || (StringUtils.isBlank(q))) {
            recipes = recipeService.getUserRecipesByPage(page, recipesOnOnePage, authentication.getName());
            pages = (int) Math.ceil((double) recipeService.getAllUserRecipes(authentication.getName()).size() / recipesOnOnePage);
        } else {
            recipes = recipeService.getUserRecipesByPageAndSearch(page, recipesOnOnePage, q, authentication.getName());
            pages = (int) Math.ceil((double) recipeService.getNumberOfAllSearchedUserRecipes(q, authentication.getName()) / recipesOnOnePage);
        }
        if (pages == 0) {
            pages++;
        }
        theModel.addAttribute("recipes", recipes);
        theModel.addAttribute("pages", pages);
        theModel.addAttribute("pageTitle", "My Recipe - Page " + page);
        return "my-recipe";
    }

    @PostMapping(value = "/recipe/{recipeId}/favourite")
    public String favouriteRecipe(@PathVariable int recipeId, Authentication authentication) {
        recipeService.changeFavouriteRecipeStatus(recipeId, authentication.getName());
        return "redirect:/recipe/{recipeId}";
    }

    @GetMapping(value = "cookbook/page/{page}")
    public String myCookbook(@PathVariable int page, Model theModel, @RequestParam(required = false, name = "q") String q, Authentication authentication) {
        int recipesOnOnePage = 12, pages;
        List<Recipe> recipes;
        if ((q == null) || (StringUtils.isBlank(q))) {
            recipes = recipeService.getUserFavouriteRecipesByPage(page, recipesOnOnePage, authentication.getName());
            pages = (int) Math.ceil((double) recipeService.getAllUserFavouriteRecipes(authentication.getName()).size() / recipesOnOnePage);
        } else {
            recipes = recipeService.getUserFavouriteRecipesByPageAndSearch(page, recipesOnOnePage, q, authentication.getName());
            pages = (int) Math.ceil((double) recipeService.getNumberOfAllSearchedUserFavouriteRecipes(q, authentication.getName()) / recipesOnOnePage);
        }
        if (pages == 0) {
            pages++;
        }
        theModel.addAttribute("recipes", recipes);
        theModel.addAttribute("pages", pages);
        theModel.addAttribute("pageTitle", "My Cookbook - Page " + page);
        return "cookbook";
    }

    @GetMapping(value = "advanced-search")
    public String advancedSearch() {
        return "advanced-search";
    }

    @PostMapping(value = "advanced-search/results/page/{page}")
    public String advancedSearchResults(@RequestParam(value = "ingredient-li", required = false) List<String> ingredients, Model theModel, @PathVariable int page) {
        int recipesOnOnePage = 12, pages;
        List<Recipe> recipes;
        recipes = recipeService.getAdvancedSearchRecipesByPageAndSearch(page, recipesOnOnePage, ingredients);
        pages = (int) Math.ceil((double) recipeService.getNumberOfAllAdvancedSearchedRecipes(ingredients) / recipesOnOnePage);
        theModel.addAttribute("recipes", recipes);
        theModel.addAttribute("pages", pages);
        theModel.addAttribute("pageTitle", "My Cookbook - Advanced Search - Page " + page);
        return "advanced-search-results";
    }
}
