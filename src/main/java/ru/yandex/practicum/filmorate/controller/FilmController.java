package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос на создание фильма: {}", film.toString());
        film = filmService.create(film);
        return film;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film read(@PathVariable("id") Long id) {
        log.info("Получен запрос на получение фильма с идентификатором: {}", id);
        return filmService.read(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на изменение фильма: {}", newFilm.toString());
        return filmService.update(newFilm);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление фильма с идентификатором: {}", id);
        filmService.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение списка всех фильмов");
        return filmService.getAll();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10") Long count) {
        log.info("Вызван метод GET /films/popular с count = {}", count);
        List<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("Метод GET /films/popular вернул ответ {}", popularFilms);
        return popularFilms;
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable("id") Long filmId,
                        @PathVariable("userId") Long userId) {
        log.info("Вызван метод PUT /films/{id}/like/{userId} с id = {} и userId = {}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("Метод PUT /films/{id}/like/{userId} успешно выполнен");
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable("id") Long filmId,
                           @PathVariable("userId") Long userId) {
        log.info("Вызван метод DELETE /films/{id}/like/{userId} с id = {} и userId = {}", filmId, userId);
        filmService.removeLike(filmId, userId);
        log.info("Метод DELETE /films/{id}/like/{userId} успешно выполнен");
    }

}