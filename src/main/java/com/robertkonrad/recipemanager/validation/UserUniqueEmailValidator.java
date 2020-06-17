package com.robertkonrad.recipemanager.validation;

import com.robertkonrad.recipemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserUniqueEmailValidator implements ConstraintValidator<UserUniqueEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UserUniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userService.emailAvailable(value);
    }
}
