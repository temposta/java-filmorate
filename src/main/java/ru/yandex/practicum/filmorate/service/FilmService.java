package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmRepository repository;

    public FilmService(FilmRepository repository) {
        this.repository = repository;
    }

    public Film createFilm(Film film) {
        log.info("Create Film: {} - Started", film);
        repository.add(film);
        log.info("Create Film: {} - Created", film);
        return film;
    }

    public Film updateFilm(Film film) {
        log.info("Update Film: {} - Started", film);
        Film updatedFilm = repository.update(film);
        log.info("Update Film: {} - Updated", film);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        log.info("Get All Films");
        return repository.getAll();
    }

    public Film deleteFilm(Film film) {
        log.info("Delete Film: {} - Started", film);
        Film deletedFilm = repository.delete(film);
        log.info("Delete Film: {} - Deleted", film);
        return deletedFilm;
    }

    public Film getFilmById(long id) {
        log.info("Get Film by id: {} - Started", id);
        Film foundFilm = repository.findById(id);
        log.info("Get Film by id: {} - Found", id);
        return foundFilm;
    }

    public Film addLike(long id, long userId) {
        log.info("Add Like to Film: {} - Started", id);
        Film film = repository.findById(id);
        film.getLikes().add(userId);
        repository.update(film);
        log.info("Add Like to Film: {} - Added", id);
        return film;
    }

    public Film deleteLike(long id, long userId) {
        log.info("Delete Like from Film: {} - Started", id);
        Film film = repository.findById(id);
        film.getLikes().remove(userId);
        repository.update(film);
        log.info("Delete Like from Film: {} - Deleted", id);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Get Popular Films");
        List<Film> popularFilms = repository.getAll()
                .stream()
                .filter(film -> !film.getLikes().isEmpty())
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .collect(Collectors.toList());
        if (popularFilms.size() < count) {
            return popularFilms;
        }
        return popularFilms.subList(0, count);
    }
}
