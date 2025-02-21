package ru.yandex.practicum.filmorate.repository.film;

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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"classpath:schema.sql", "classpath:test-data.sql"})
@Import({FilmRepositoryImpl.class})
@DisplayName("тестирование FilmRepositoryImpl")
class FilmRepositoryImplTest {

    @Autowired
    FilmRepository filmRepository;
    Film film;

    @BeforeEach
    void setUp() {
        film = Film.builder()
                .name("Film 1")
                .description("Film description")
                .releaseDate(LocalDate.parse("2000-01-01"))
                .mpa(Mpa.builder().id(3L).build())
                .duration(120)
                .build();
    }

    @Test
    @DisplayName("добавление фильма в репозиторий")
    void add() {
        assertNull(film.getId());
        Film addingFilm = filmRepository.add(film);
        assertNotNull(addingFilm);
        assertNotNull(addingFilm.getId());
    }

    @Test
    @DisplayName("проверка обновления фильма")
    void update() {
        film.setId(4L);
        filmRepository.update(film);
        Film updatingFilm = filmRepository.findById(4L);
        assertNotNull(updatingFilm);
        assertEquals(updatingFilm.getId(), film.getId());
        assertEquals(updatingFilm.getName(), film.getName());
        assertEquals(updatingFilm.getDescription(), film.getDescription());
        assertEquals(updatingFilm.getReleaseDate(), film.getReleaseDate());
        assertEquals(updatingFilm.getMpa().getId(), film.getMpa().getId());
        assertEquals(updatingFilm.getDuration(), film.getDuration());

    }

    @Test
    @DisplayName("удаление фильма без ошибок")
    void delete() {
        Film deletingFilm = Film.builder().id(5L).build();
        assertDoesNotThrow(() -> filmRepository.delete(deletingFilm));
        assertNull(filmRepository.findById(5L));
    }

    @Test
    @DisplayName("получение полного списка фильмов")
    void getAll() {
        List<Film> films = filmRepository.getAll();
        assertNotNull(films);
        System.out.println("получение списка фильмов");
        assertEquals(films.size(), 5);
        films.forEach(System.out::println);
    }

    @Test
    @DisplayName("получение трех самых популярных фильмов")
    void getThreePopularFilms() {
        List<Film> popular = filmRepository.getPopular(3);
        assertNotNull(popular);
        assertEquals(popular.size(), 3);
        assertEquals(popular.get(0).getRate(), 6);
        assertEquals(popular.get(1).getRate(), 3);
        assertEquals(popular.get(2).getRate(), 3);
    }

    @Test
    @DisplayName("получение ста самых популярных фильмов")
    void getHundredPopularFilms() {
        List<Film> popular = filmRepository.getPopular(100);
        assertNotNull(popular);
        assertEquals(popular.size(), 5);
    }

    @Test
    @DisplayName("проверка существования фильма")
    void checkFilm() {
        assertThrows(EmptyResultDataAccessException.class, () -> filmRepository.checkFilm(555L));
        assertThrows(EmptyResultDataAccessException.class, () -> filmRepository.checkFilm(null));
    }

    @Test
    @DisplayName("поиск фильма по id")
    void findById() {
        Film findingFilm = filmRepository.findById(5L);
        assertNotNull(findingFilm);
        assertEquals(findingFilm.getId(), 5L);
        assertEquals(findingFilm.getName(), "film4");
        assertEquals(findingFilm.getDescription(), "description4");
        assertEquals(findingFilm.getReleaseDate(), LocalDate.parse("2000-01-13"));
        assertEquals(findingFilm.getMpa().getId(), 4);
        assertEquals(findingFilm.getDuration(), 100);

    }

    @Test
    @DisplayName("поиск фильма по неверному id")
    void findByWrongId() {
        Film findingFilm = filmRepository.findById(555L);
        assertNull(findingFilm);
    }
}