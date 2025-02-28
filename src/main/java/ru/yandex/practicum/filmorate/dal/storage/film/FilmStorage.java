package ru.yandex.practicum.filmorate.dal.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    Optional<Film> read(Long filmId);

    Film update(Film film);

    void delete(Long filmId);

    List<Film> getAll();

    List<Film> getPopularFilms(Long count);

}
