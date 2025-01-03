package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFilmRepositoryImpl implements FilmRepository {

    private int counter = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(++counter);
        return films.put(film.getId(), film);
    }

    @Override
    public Film updateFilm(Film film) {
        Film oldFilm = films.get(film.getId());
        if (oldFilm != null) {
            String newName = film.getName();
            if (newName != null) oldFilm.setName(newName);
            String newDescription = film.getDescription();
            if (newDescription != null) oldFilm.setDescription(newDescription);
            LocalDate newReleaseDate = film.getReleaseDate();
            if (newReleaseDate != null) oldFilm.setReleaseDate(newReleaseDate);
            int newDuration = film.getDuration();
            if (newDuration > 0) oldFilm.setDuration(newDuration);
            return oldFilm;
        }
        return null;
    }

    @Override
    public Film deleteFilm(Film film) {
        return films.remove(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

}
