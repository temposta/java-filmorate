package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("проверка валидации поля Email по шаблону")
    void testEmailTemplateValidation() {
        User user = new User();
        user.setEmail("testtestcom");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля Email на пустоту")
    void testEmailNotBlankValidation() {
        User user = new User();
        user.setEmail("");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля Login")
    void testLoginValidation() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setLogin("log in");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля Birthday")
    void testBirthdayValidation() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }
}