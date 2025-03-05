package ru.yandex.practicum.filmorate.dal.storage.film;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.yandex.practicum.filmorate.enums.SearchValues;
import ru.yandex.practicum.filmorate.enums.SortValue;
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

    List<Film> search(String query, List<SearchValues> by);

    List<Film> search(@NotNull @Positive Long directorId, SortValue sortValue);
}
