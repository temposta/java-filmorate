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
    @DisplayName("работа валидации данных модели Film")
    void testFilmValidations() {
        Film film = new Film();
        film.setDescription("as;dlkjfa;sdlfkjassdlfkjasd;flaskdjfa;sd;flasdlfkjasd;flaskdjfa;sskdjfa;sdlkfja;sdlkf" +
                "jas;dlkfjas;dklfjas;dkfjas;dsdlfkjasd;flaskdjfa;skfjasdlfkjasd;flaskdjfa;ss;dkfjas;dkfjas;dk" +
                "asdfasdfasdfasdfasdfasdfasdsdlfkjasd;flaskdjfa;sfasdlfkjasd;flaskdjfa;sdaasdfasdfasdfasdf" +
                "asdfadfasdfasdfasdfadfasdfasdlfkjasd;flaskdjfa;ssdfasdfasdfasfasdfjasd");
        film.setDuration(-20);
        film.setName(" ");
        film.setReleaseDate(LocalDate.of(1700, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " " + e.getMessage()));
        assertEquals(4, violations.size());
    }

}