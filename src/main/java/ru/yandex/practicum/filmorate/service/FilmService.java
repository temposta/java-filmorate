package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film read(Long id) {
        return filmStorage.read(id);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public Film update(Film film) throws NotFoundException {
        if (filmStorage.read(film.getId()) == null) {
            throw new NotFoundException(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, film.getId()));
        }
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getPopularFilms(count);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = Optional.ofNullable(filmStorage.read(filmId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, filmId)));
        User user = Optional.ofNullable(userStorage.read(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        filmStorage.addLike(film, user);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = Optional.ofNullable(filmStorage.read(filmId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, filmId)));
        User user = Optional.ofNullable(userStorage.read(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        filmStorage.removeLike(film, user);
    }

}