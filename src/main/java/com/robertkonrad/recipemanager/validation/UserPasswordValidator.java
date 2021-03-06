package com.robertkonrad.recipemanager.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserPasswordValidator implements ConstraintValidator<UserPassword, String> {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void initialize(UserPassword password) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext theConstraintValidatorContext) {
        return validatePassword(password);
    }

    private boolean validatePassword(String password) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
