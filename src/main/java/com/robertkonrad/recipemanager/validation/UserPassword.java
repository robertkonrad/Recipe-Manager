package com.robertkonrad.recipemanager.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPassword {
    public String value() default "";

    public String message() default "Invalid password. Password must have at least one digit," +
            " one lower case letter, one upper case letter, one special digit, no whitespaces. Minimal length must be 8 chars.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
