package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.dal.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dal.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.dal.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final RatingStorage ratingStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;
    private final FilmRepository filmRepository;

    public Film create(Film film) {

        Optional<Mpa> rating = ratingStorage.read(film.getMpa().getId());
        if (rating.isEmpty()) {
            log.error(String.format(ExceptionMessages.RATING_NOT_FOUND_ERROR, film.getMpa().getId()));
            throw new NotFoundException(String.format(ExceptionMessages.RATING_NOT_FOUND_ERROR, film.getMpa().getId()));
        }

        if (film.getGenres() != null) {
            List<Genre> genres = genreStorage.getAll();
            film.getGenres().forEach(genre -> {
                Long id = genre.getId();
                if (genres.stream().filter(g -> Objects.equals(g.getId(), id)).findFirst().isEmpty()) {
                    log.error(String.format(ExceptionMessages.GENRE_NOT_FOUND_ERROR, id));
                    throw new NotFoundException(String.format(ExceptionMessages.GENRE_NOT_FOUND_ERROR, id));
                }
            });
        }

        film = filmStorage.create(film);
        log.info("Фильм создан {}", film);
        return film;
    }

    public Film update(Film film) throws NotFoundException {
        if (film.getId() == null) {
            log.error("Не указан id фильма {}", film);
            throw new ValidationException("Id должен быть указан");
        }
        if (filmStorage.read(film.getId()).isEmpty()) {
            log.error(ExceptionMessages.FILM_NOT_FOUNT_ERROR, film);
            throw new NotFoundException(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, film.getId()));
        }

        Optional<Mpa> rating = ratingStorage.read(film.getMpa().getId());
        if (rating.isEmpty()) {
            log.error(String.format(ExceptionMessages.RATING_NOT_FOUND_ERROR, film.getMpa().getId()));
            throw new NotFoundException(String.format(ExceptionMessages.RATING_NOT_FOUND_ERROR, film.getMpa().getId()));
        }

        if (film.getGenres() != null) {
            List<Genre> genres = genreStorage.getAll();
            film.getGenres().forEach(genre -> {
                Long id = genre.getId();
                if (genres.stream().filter(g -> Objects.equals(g.getId(), id)).findFirst().isEmpty()) {
                    log.error(String.format(ExceptionMessages.GENRE_NOT_FOUND_ERROR, id));
                    throw new NotFoundException(String.format(ExceptionMessages.GENRE_NOT_FOUND_ERROR, id));
                }
            });
        }

        film = filmStorage.update(film);
        log.info("Фильм обновлен {}", film);
        return film;
    }

    private void updateRating(Film film) {
        if (film.getMpa() != null) {
            film.setMpa(ratingStorage.getAll().stream()
                    .filter(o -> Objects.equals(o.getId(), film.getMpa().getId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.RATING_NOT_FOUND_ERROR, film.getMpa().getId()))));
        }
    }


    private void updateGenre(Film film) {
        if (film.getGenres() != null) {

            Set<Genre> newGenres = new HashSet<>();
            film.getGenres().forEach(g -> {
                newGenres.add(genreStorage.getAll().stream()
                        .filter(o -> Objects.equals(o.getId(), g.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.GENRE_NOT_FOUND_ERROR, g.getId()))));
            });

            film.setGenres(newGenres);
        }
    }

    public Film read(Long id) {
        Optional<Film> film = filmStorage.read(id);
        if (film.isPresent()) {
            return film.get();
        }
        log.error(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, id));
        throw new NotFoundException(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, id));
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(Long filmId, Long userId) {
        Optional<Film> film = filmStorage.read(filmId);
        if (film.isEmpty()) {
            log.error(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, filmId));
            throw new NotFoundException(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, filmId));
        }

        Optional<User> user = userStorage.read(userId);
        if (user.isEmpty()) {
            log.error(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId));
            throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId));
        }

        likeStorage.create(userId, filmId);
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        Optional<Film> film = filmStorage.read(filmId);
        if (film.isEmpty()) {
            log.error(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, filmId));
            throw new NotFoundException(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, filmId));
        }

        Optional<User> user = userStorage.read(userId);
        if (user.isEmpty()) {
            log.error(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId));
            throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId));
        }

        likeStorage.delete(userId, filmId);
        log.info("Пользователь с id = {} убрал лайк с фильма с id = {}", userId, filmId);
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getPopularFilms(count);
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        List<Film> commonFilms = filmRepository.getCommonFilms(userId, friendId);
        log.info("Получен список общих фильмов. Количество: {}", commonFilms.size());
        return commonFilms;
    }

}