package com.robertkonrad.recipemanager.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserUniqueEmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserUniqueEmail {
    public String value() default "";

    public String message() default "There is an account with that email address!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
