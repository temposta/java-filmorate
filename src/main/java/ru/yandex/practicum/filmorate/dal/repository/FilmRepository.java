package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.SearchValues;
import ru.yandex.practicum.filmorate.enums.SortValue;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_BY_ID_QUERY = """
            SELECT f.*, mpa.name as mpa_name,
                    string_agg(dir.id, ', ') as dir_ids, string_agg(dir.name, ', ') as dir_names,
                    string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names
            FROM film f
            LEFT JOIN film_genre fg on fg.film_id = f.id
            LEFT JOIN genre g on g.id = fg.genre_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN film_director fd on fd.film_id = f.id
            LEFT JOIN director dir on dir.id = fd.director_id
            WHERE f.id = ?
            GROUP BY f.id""";
    private static final String FIND_ALL_QUERY = """
            SELECT f.*, mpa.name as mpa_name,
                    string_agg(dir.id, ', ') as dir_ids, string_agg(dir.name, ', ') as dir_names,
                    string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names
            FROM film f
            LEFT JOIN film_genre fg on fg.film_id = f.id
            LEFT JOIN genre g on g.id = fg.genre_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN film_director fd on fd.film_id = f.id
            LEFT JOIN director dir on dir.id = fd.director_id
            GROUP BY f.id""";
    private static final String FIND_POPULAR_QUERY = """
            SELECT f.*, mpa.name as mpa_name,
                    string_agg(dir.id, ', ') as dir_ids, string_agg(dir.name, ', ') as dir_names,
                    string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names
            FROM film f
            LEFT JOIN film_genre fg on fg.film_id = f.id
            LEFT JOIN genre g on g.id = fg.genre_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN film_director fd on fd.film_id = f.id
            LEFT JOIN director dir on dir.id = fd.director_id
            LEFT JOIN likes l on l.film_id = f.id
            GROUP BY f.id
            ORDER BY count(l.user_id) DESC limit ?""";
    private static final String INSERT_QUERY = """
            INSERT INTO film (name, description, release_date, duration, rating)
            VALUES (?, ?, ?, ?, ?)""";
    private static final String DELETE_QUERY = """
            DELETE FROM film WHERE id = ?""";
    private static final String INSERT_GENRES_QUERY = """
            INSERT INTO film_genre (film_id, genre_id)
            VALUES (?, ?)""";
    private static final String UPDATE_QUERY = """
            UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating = ?
            WHERE id = ?""";
    private static final String SEARCH_BY_QUERY = """
            SELECT f.*, mpa.name as mpa_name,
                    string_agg(dir.id, ', ') as dir_ids, string_agg(dir.name, ', ') as dir_names,
                    string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names
            FROM film f
            LEFT JOIN film_genre fg on fg.film_id = f.id
            LEFT JOIN genre g on g.id = fg.genre_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN film_director fd on fd.film_id = f.id
            LEFT JOIN director dir on dir.id = fd.director_id
            LEFT JOIN likes l on l.film_id = f.id
            WHERE f.name ILIKE ? OR dir.name ILIKE ?
            GROUP BY f.id
            ORDER BY count(l.user_id) DESC""";
    private static final String INSERT_DIRECTORS_QUERY = """
            INSERT INTO film_director (film_id, director_id)
            VALUES (?, ?)""";
    private static final String SEARCH_BY_DIR_YEAR_SORT = """
            SELECT f.*, mpa.name as mpa_name,
                    string_agg(dir.id, ', ') as dir_ids, string_agg(dir.name, ', ') as dir_names,
                    string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names
            FROM film f
            LEFT JOIN film_genre fg on fg.film_id = f.id
            LEFT JOIN genre g on g.id = fg.genre_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN film_director fd on fd.film_id = f.id
            LEFT JOIN director dir on dir.id = fd.director_id
            LEFT JOIN likes l on l.film_id = f.id
            WHERE f.ID in (
                    SELECT DISTINCT fd.FILM_ID
                    FROM film_director fd
                    WHERE fd.DIRECTOR_ID = ?)
            GROUP BY f.id, f.RELEASE_DATE
            ORDER BY extract(YEAR FROM f.RELEASE_DATE)
            """;
    private static final String SEARCH_BY_DIR_LIKES_SORT = """
            SELECT f.*, mpa.name as mpa_name,
                    string_agg(dir.id, ', ') as dir_ids, string_agg(dir.name, ', ') as dir_names,
                    string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names
            FROM film f
            LEFT JOIN film_genre fg on fg.film_id = f.id
            LEFT JOIN genre g on g.id = fg.genre_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN film_director fd on fd.film_id = f.id
            LEFT JOIN director dir on dir.id = fd.director_id
            LEFT JOIN likes l on l.film_id = f.id
            WHERE f.ID in (
                    SELECT DISTINCT fd.FILM_ID
                    FROM film_director fd
                    WHERE fd.DIRECTOR_ID = ?)
            GROUP BY f.id
            ORDER BY count(l.user_id) DESC
            """;
    private static final String DELETE_GENRES_DIRECTORS_QUERY = """
            DELETE FROM film_genre WHERE film_id = ?;
            DELETE FROM film_director WHERE film_id = ?;""";
    private static final String GET_COMMON_FILMS = """
            SELECT f.*, mpa.name as mpa_name,
                string_agg(dir.id, ', ') as dir_ids, string_agg(dir.name, ', ') as dir_names,
                string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names
            FROM film f
            LEFT JOIN film_genre fg on fg.film_id = f.id
            LEFT JOIN genre g on g.id = fg.genre_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN film_director fd on fd.film_id = f.id
            LEFT JOIN director dir on dir.id = fd.director_id
            LEFT JOIN likes l on l.film_id = f.id
            WHERE f.ID in (
                    SELECT FILM_ID
                    FROM LIKES
                    WHERE USER_ID IN (?, ?)
                    GROUP BY FILM_ID
                    HAVING COUNT(FILM_ID) > 1)
            GROUP BY f.id
            ORDER BY count(l.user_id) DESC
            """;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public List<Film> findPopular(Long count) {
        return findMany(FIND_POPULAR_QUERY, count);
    }

    public Film create(Film film) {
        Long id = insertWithGeneratedId(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay().toInstant(ZoneOffset.UTC)),
                film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId());
        film.setId(id);
        insertGenresAndDirectors(film, id);
        return film;
    }

    public Film update(Film film) {
        Long id = film.getId();
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay().toInstant(ZoneOffset.UTC)),
                film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId(),
                id);
        delete(DELETE_GENRES_DIRECTORS_QUERY, id, id);
        insertGenresAndDirectors(film, id);
        return film;
    }

    public boolean delete(Long id) {
        return delete(DELETE_QUERY, id);
    }

    public List<Film> search(String query, List<SearchValues> by) {
        String namePattern = by.contains(SearchValues.TITLE) ? "%" + query + "%" : "";
        String dirPattern = by.contains(SearchValues.DIRECTOR) ? "%" + query + "%" : "";
        return findMany(SEARCH_BY_QUERY, namePattern, dirPattern);
    }

    public List<Film> search(Long directorId, SortValue sortValue) {
        switch (sortValue) {
            case YEAR -> {
                return findMany(SEARCH_BY_DIR_YEAR_SORT, directorId);
            }
            case LIKES -> {
                return findMany(SEARCH_BY_DIR_LIKES_SORT, directorId);
            }
            default -> throw new IllegalStateException("Unexpected value: " + sortValue);
        }
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return jdbc.query(GET_COMMON_FILMS, mapper, userId, friendId);
    }

    private void insertGenresAndDirectors(Film film, Long id) {
        if (film.getGenres() != null) {
            List<Object[]> batchParam = new ArrayList<>(List.of());
            film.getGenres().forEach(genre -> batchParam.add(new Object[]{id, genre.getId()}));
            jdbc.batchUpdate(INSERT_GENRES_QUERY, batchParam);
        }
        if (film.getDirectors() != null) {
            List<Object[]> batchParam = new ArrayList<>(List.of());
            film.getDirectors().forEach(director -> batchParam.add(new Object[]{id, director.getId()}));
            jdbc.batchUpdate(INSERT_DIRECTORS_QUERY, batchParam);
        }
    }

}