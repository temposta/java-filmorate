package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dal.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dal.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.dal.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private FilmStorage filmStorage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private RatingStorage ratingStorage;
    @Mock
    private GenreStorage genreStorage;
    @Mock
    private LikeStorage likeStorage;

    @InjectMocks
    private FilmService filmService;

    private Film testFilm;
    private User testUser;

    @BeforeEach
    void setUp() {
        testFilm = Film.builder()
                .id(1L)
                .name("Test Film")
                .description("Test Description")
                .mpa(new Mpa(1L, "G"))
                .genres(Set.of(new Genre(1L, "Комедия")))
                .build();

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(2000, 6, 15))
                .build();
    }

    // ------------------------- Тесты для create() -------------------------
    @Test
    void createFilm_WithInvalidMpa_ThrowsNotFoundException() {
        when(ratingStorage.read(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.create(testFilm)
        );

        assertEquals(String.format(ExceptionMessages.RATING_NOT_FOUND_ERROR, 1L), exception.getMessage());
    }

    @Test
    void createFilm_WithInvalidGenre_ThrowsNotFoundException() {
        when(ratingStorage.read(anyLong())).thenReturn(Optional.of(new Mpa(1L, "G")));
        when(genreStorage.getAll()).thenReturn(List.of(new Genre(2L, "Драма")));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.create(testFilm)
        );

        assertEquals(String.format(ExceptionMessages.GENRE_NOT_FOUND_ERROR, 1L), exception.getMessage());
    }

    @Test
    void createFilm_Success() {
        when(ratingStorage.read(anyLong())).thenReturn(Optional.of(new Mpa(1L, "G")));
        when(genreStorage.getAll()).thenReturn(List.of(new Genre(1L, "Комедия")));
        when(filmStorage.create(any(Film.class))).thenReturn(testFilm);

        Film result = filmService.create(testFilm);

        assertEquals(testFilm, result);
        verify(filmStorage, times(1)).create(testFilm);
    }

    // ------------------------- Тесты для update() -------------------------
    @Test
    void updateFilm_WithoutId_ThrowsValidationException() {
        testFilm.setId(null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.update(testFilm)
        );

        assertEquals("Id должен быть указан", exception.getMessage());
    }

    @Test
    void updateFilm_WithNonExistingId_ThrowsNotFoundException() {
        when(filmStorage.read(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.update(testFilm)
        );

        assertEquals(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, 1L), exception.getMessage());
    }

    @Test
    void updateFilm_Success() {
        when(filmStorage.read(anyLong())).thenReturn(Optional.of(testFilm));
        when(ratingStorage.read(anyLong())).thenReturn(Optional.of(new Mpa(1L, "G")));
        when(genreStorage.getAll()).thenReturn(List.of(new Genre(1L, "Комедия")));
        when(filmStorage.update(any(Film.class))).thenReturn(testFilm);

        Film result = filmService.update(testFilm);

        assertEquals(testFilm, result);
        verify(filmStorage, times(1)).update(testFilm);
    }

    // ------------------------- Тесты для read() -------------------------
    @Test
    void readFilm_WithNonExistingId_ThrowsNotFoundException() {
        when(filmStorage.read(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.read(1L)
        );

        assertEquals(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, 1L), exception.getMessage());
    }

    // ------------------------- Тесты для addLike() -------------------------
    @Test
    void addLike_WithNonExistingUser_ThrowsNotFoundException() {
        when(filmStorage.read(anyLong())).thenReturn(Optional.of(testFilm));
        when(userStorage.read(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.addLike(1L, 1L)
        );

        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 1L), exception.getMessage());
    }

    @Test
    void addLike_Success() {
        when(filmStorage.read(anyLong())).thenReturn(Optional.of(testFilm));
        when(userStorage.read(anyLong())).thenReturn(Optional.of(testUser));

        filmService.addLike(1L, 1L);

        verify(likeStorage, times(1)).create(1L, 1L);
    }

    // ------------------------- Тесты для removeLike() -------------------------
    @Test
    void removeLike_WithNonExistingUser_ThrowsNotFoundException() {
        when(filmStorage.read(anyLong())).thenReturn(Optional.of(testFilm));
        when(userStorage.read(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.removeLike(1L, 1L)
        );

        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 1L), exception.getMessage());
    }

    // ------------------------- Тесты для getPopularFilms() -------------------------
    @Test
    void getPopularFilms_Success() {
        List<Film> expectedFilms = List.of(testFilm);
        when(filmStorage.getPopularFilms(anyLong())).thenReturn(expectedFilms);

        List<Film> result = filmService.getPopularFilms(10L);

        assertEquals(expectedFilms, result);
        verify(filmStorage, times(1)).getPopularFilms(10L);
    }
}