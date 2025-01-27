package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film read(Long filmId);

    Film update(Film film);

    void delete(Long filmId);

    List<Film> getAll();

    List<Film> getPopularFilms(Long count);

    void addLike(Film film, User user);

    void removeLike(Film film, User user);
}
