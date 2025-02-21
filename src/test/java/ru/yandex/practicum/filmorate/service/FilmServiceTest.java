package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.film.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.film.like.LikeRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"classpath:schema.sql", "classpath:test-data.sql"})
@Import({FilmService.class, FilmRepositoryImpl.class, LikeRepositoryImpl.class})
@DisplayName("тестирование FilmService")
class FilmServiceTest {

    @Autowired
    private final FilmService filmService;
    private Film film;

    @BeforeEach
    void setUp() {
        film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Description")
                .duration(120)
                .releaseDate(LocalDate.parse("2000-01-01"))
                .mpa(Mpa.builder().id(1L).build())
                .build();
    }

    @Test
    @DisplayName("добавление фильма")
    void createFilm() {
        Film createdFilm = filmService.createFilm(film);
        assertThat(createdFilm)
                .isNotNull()
                .isEqualTo(film);
        System.out.println("createdFilm = " + createdFilm);
    }

    @Test
    @DisplayName("обновление фильма")
    void updateFilm() {
        film.setId(2L);
        Film updatedFilm = filmService.updateFilm(film);
        assertThat(updatedFilm)
                .isNotNull()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("обновление фильма с неверным id")
    void updateFilmWithWrongId() {
        film.setId(222L);
        assertThrows(EmptyResultDataAccessException.class, () -> filmService.updateFilm(film));
    }

    @Test
    @DisplayName("получение списка всех фильмов")
    void getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
        films.forEach(System.out::println);
    }

    @Test
    @DisplayName("удаление фильма")
    void deleteFilm() {
        film.setId(5L);
        assertDoesNotThrow(() -> filmService.deleteFilm(film));
        assertThat(filmService.getFilmById(5L))
                .isNull();
    }

    @Test
    @DisplayName("поиск фильма по id")
    void getFilmById() {
        assertThat(filmService.getFilmById(4L))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("name", "film3")
                .hasFieldOrPropertyWithValue("description", "description3")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.parse("2000-01-12"))
                .hasFieldOrPropertyWithValue("mpa", Mpa.builder().id(3L).name("PG-13").build())
                .hasFieldOrPropertyWithValue("duration", 110)
                .hasFieldOrPropertyWithValue("rate", 2L);
    }

    @Test
    @DisplayName("добавление лайка")
    void addLike() {
        assertDoesNotThrow(() -> filmService.addLike(4L, 5L));
    }

    @Test
    @DisplayName("удаление лайка")
    void deleteLike() {
        assertDoesNotThrow(() -> filmService.deleteLike(4L, 4L));
    }

    @Test
    @DisplayName("получение 4х самых популярных фильмов")
    void getPopularFilms() {
        List<Film> popular = filmService.getPopularFilms(4);
        assertThat(popular)
                .isNotNull()
                .isNotEmpty()
                .hasSize(4);
        assertThat(popular.get(0).getRate())
                .isEqualTo(6);
        assertThat(popular.get(1).getRate())
                .isEqualTo(3);
        assertThat(popular.get(2).getRate())
                .isEqualTo(3);
        assertThat(popular.get(3).getRate())
                .isEqualTo(2);
    }
}