package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.ExceptionHandlers.getResponse;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final FilmRepository repository;

    public FilmController(FilmRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Film saveFilm(@RequestBody @Valid Film film) {
        log.info("Create Film: {} - Started", film);
        repository.addFilm(film);
        log.info("Create Film: {} - Created", film);
        return film;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Update Film: {} - Started", film);
        Film updatedFilm = repository.updateFilm(film);
        log.info("Update Film: {} - Updated", film);
        return updatedFilm;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Film> getAllFilms() {
        log.info("Get All Films");
        return repository.getAllFilms();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    void deleteFilm(@RequestBody @Valid Film film) {
        log.info("Delete Film: {} - Started", film);
        repository.deleteFilm(film);
        log.info("Delete Film: {} - Deleted", film);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        return getResponse(ex, log);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleValidationException(NullPointerException ex) {
        return getResponse(ex, log);
    }

}
