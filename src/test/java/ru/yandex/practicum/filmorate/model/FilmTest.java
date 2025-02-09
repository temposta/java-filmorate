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

class FilmTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("проверка валидации поля Description")
    void testDescriptionValidation() {
        Film film = Film.builder().build();
        film.setDescription("a".repeat(210));
        film.setDuration(10);
        film.setName("name");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля Duration")
    void testDurationValidation() {
        Film film = Film.builder().build();
        film.setDescription("a".repeat(10));
        film.setDuration(-10);
        film.setName("name");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля Name")
    void testNameValidation() {
        Film film = Film.builder().build();
        film.setDescription("a".repeat(10));
        film.setDuration(10);
        film.setName(" ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("проверка валидации поля ReleaseDate")
    void testReleaseDateValidation() {
        Film film = Film.builder().build();
        film.setDescription("a".repeat(10));
        film.setDuration(10);
        film.setName("name");
        film.setReleaseDate(LocalDate.of(1700, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(1, violations.size());
    }

}