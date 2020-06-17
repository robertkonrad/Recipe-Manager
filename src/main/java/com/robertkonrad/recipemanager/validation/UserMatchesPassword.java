package com.robertkonrad.recipemanager.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserMatchesPasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserMatchesPassword {
    public String value() default "";

    public String message() default "Passwords don'''t match!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

