package com.nissum.security.service.validator;

import com.nissum.security.domain.annotations.NissumPasswordValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<NissumPasswordValidator, String> {

    @Value("${password.regex.pattern}")
    private String regex;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!Strings.isBlank(value)) {
            Pattern compile = Pattern.compile(regex);
            return compile.matcher(value).matches();
        }
        return false;
    }
}
