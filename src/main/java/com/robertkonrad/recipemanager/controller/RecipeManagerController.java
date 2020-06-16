package com.robertkonrad.recipemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RecipeManagerController {

    @RequestMapping(value = "/")
    public String main() {
        return "test";
    }
}
