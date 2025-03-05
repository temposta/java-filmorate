package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.SearchValues;
import ru.yandex.practicum.filmorate.enums.SortValue;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Timestamp;
import java.time.ZoneOffset;
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
    private static final String DELETE_GENRES_QUERY = """
            DELETE FROM film_genre WHERE film_id = ?""";
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
            WHERE f.name LIKE ? OR dir.name LIKE ?
            GROUP BY f.id
            ORDER BY count(l.user_id) DESC""";
    private static final String DELETE_DIRECTORS_QUERY = """
            DELETE FROM film_director WHERE film_id = ?""";
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
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> insert(INSERT_GENRES_QUERY, id, genre.getId()));
        }
        if (film.getDirectors() != null) {
            film.getDirectors().forEach(director -> insert(INSERT_DIRECTORS_QUERY, id, director.getId()));
        }
        return film;
    }

    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay().toInstant(ZoneOffset.UTC)),
                film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId(),
                film.getId());

        delete(DELETE_GENRES_QUERY, film.getId());
        delete(DELETE_DIRECTORS_QUERY, film.getId());
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> insert(INSERT_GENRES_QUERY, film.getId(), genre.getId()));
        }
        if (film.getDirectors() != null) {
            film.getDirectors().forEach(director -> insert(INSERT_DIRECTORS_QUERY, film.getId(), director.getId()));
        }
        return film;
    }

    public void delete(Long id) {
        delete(DELETE_QUERY, id);
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
}