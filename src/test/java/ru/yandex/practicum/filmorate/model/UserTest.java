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
        User user = User.builder()
                .email("testtestcom")
                .login("login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля Email на пустоту")
    void testEmailNotBlankValidation() {
        User user = User.builder()
                .email("")
                .login("login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля Login")
    void testLoginValidation() {
        User user = User.builder()
                .email("test@email.com")
                .login("log in")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля Birthday")
    void testBirthdayValidation() {
        User user = User.builder()
                .email("test@email.com")
                .login("login")
                .birthday(LocalDate.of(2990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }
}