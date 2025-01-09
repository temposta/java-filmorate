package ru.yandex.practicum.filmorate.validator.string;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoWhiteSpaceValidator.class)
public @interface NoWhiteSpace {
    String message() default "Значение не должно содержать пробелы";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value() default "";
}
