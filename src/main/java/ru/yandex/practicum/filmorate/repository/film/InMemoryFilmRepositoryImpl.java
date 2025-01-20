package ru.yandex.practicum.filmorate.repository.film;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class InMemoryFilmRepositoryImpl implements FilmRepository {

    private Long counter = 0L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
        film.setId(++counter);
        return films.put(film.getId(), film);
    }

    @Override
    public Film update(Film film) {
        @NotNull
        Film updatableFilm = films.get(film.getId());
        String newName = film.getName();
        if (newName != null) updatableFilm.setName(newName);
        String newDescription = film.getDescription();
        if (newDescription != null) updatableFilm.setDescription(newDescription);
        LocalDate newReleaseDate = film.getReleaseDate();
        if (newReleaseDate != null) updatableFilm.setReleaseDate(newReleaseDate);
        int newDuration = film.getDuration();
        if (newDuration > 0) updatableFilm.setDuration(newDuration);
        Set<Long> newLikes = film.getLikes();
        updatableFilm.setLikes(newLikes);
        return updatableFilm;
    }

    @Override
    public Film delete(Film film) {
        return films.remove(film.getId());
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(long id) {
        @NotNull
        Film film = films.get(id);
        return film;
    }

}
