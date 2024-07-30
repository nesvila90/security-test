package com.nissum.security.domain.annotations;

import com.nissum.security.service.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tiene una longitud mínima de 8 caracteres.
 * Contiene al menos una letra minúscula.
 * Contiene al menos una letra mayúscula.
 * Contiene al menos un dígito.
 * Contiene al menos un carácter especial (como @, #, $, etc.).
 */
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NissumPasswordValidator {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
