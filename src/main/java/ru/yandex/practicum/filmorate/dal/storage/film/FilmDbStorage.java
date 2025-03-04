package ru.yandex.practicum.filmorate.dal.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.SearchValues;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.repository.FilmRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    final FilmRepository filmRepository;

    @Override
    public List<Film> getAll() {
        return filmRepository.findAll();
    }

    @Override
    public Optional<Film> read(Long filmId) {
        return filmRepository.findById(filmId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return filmRepository.findPopular(count);
    }

    @Override
    public List<Film> searchFilms(String query, List<SearchValues> by) {
        return filmRepository.searchFilms(query, by);
    }

    @Override
    public Film create(Film film) {
        return filmRepository.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmRepository.update(film);
    }

    @Override
    public void delete(Long filmId) {
        filmRepository.delete(filmId);
    }

}