package com.robertkonrad.recipemanager.validation;

import com.robertkonrad.recipemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserUniqueUsernameValidator implements ConstraintValidator<UserUniqueUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UserUniqueUsername constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userService.usernameAvailable(value);
    }
}
