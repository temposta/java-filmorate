package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {

    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film deleteFilm(Film film);

    List<Film> getAllFilms();

}
