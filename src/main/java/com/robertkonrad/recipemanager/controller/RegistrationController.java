package com.robertkonrad.recipemanager.controller;

import com.robertkonrad.recipemanager.entity.User;
import com.robertkonrad.recipemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/form")
    public String registerForm(Model theModel) {
        User user = new User();
        theModel.addAttribute("user", user);
        return "register-form";
    }

    @RequestMapping(value = "/user/save")
    public String saveUser(@ModelAttribute("user") User user, BindingResult theBindingResult) {
        if (theBindingResult.hasErrors()) {
            return "register-form";
        } else {
            userService.saveUser(user);
            return "redirect:/login";
        }
    }


}
