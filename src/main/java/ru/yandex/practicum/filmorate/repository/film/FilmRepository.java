package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.Repository;

import java.util.List;

public interface FilmRepository extends Repository<Film> {

    List<Film> getPopular(int count);

    void checkFilm(Long id);

}
