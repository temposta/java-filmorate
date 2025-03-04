package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_BY_ID_QUERY = "SELECT f.*, mpa.name as mpa_name, string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names " +
            "FROM film f " +
            "LEFT JOIN film_genre fg on fg.film_id = f.id " +
            "LEFT JOIN genre g on g.id = fg.genre_id " +
            "LEFT JOIN rating mpa on mpa.id = f.rating " +
            "WHERE f.id = ? " +
            "GROUP BY f.id";
    private static final String FIND_ALL_QUERY = "SELECT f.*, mpa.name as mpa_name, string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names " +
            "FROM film f " +
            "LEFT JOIN film_genre fg on fg.film_id = f.id " +
            "LEFT JOIN genre g on g.id = fg.genre_id " +
            "LEFT JOIN rating mpa on mpa.id = f.rating " +
            "GROUP BY f.id";
    private static final String FIND_POPULAR_QUERY = "SELECT f.*, mpa.name as mpa_name, string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names " +
            "FROM film f " +
            "LEFT JOIN film_genre fg on fg.film_id = f.id " +
            "LEFT JOIN genre g on g.id = fg.genre_id " +
            "LEFT JOIN rating mpa on mpa.id = f.rating " +
            "LEFT JOIN likes l on l.film_id = f.id " +
            "GROUP BY f.id " +
            "ORDER BY count(l.user_id) DESC limit ?";
    private static final String GET_COMMON_FILMS = "SELECT f.*, m.mpa_id AS mpa_id, m.name AS mpa_name" +
            " FROM likes AS l" +
            " JOIN films AS f ON l.film_id = f.film_id" +
            " JOIN mpa m ON f.mpa_id = m.mpa_id" +
            " WHERE l.user_id = ?" +
            " AND l.film_id IN (SELECT fl.film_id FROM likes AS fl WHERE fl.user_id = ?)" +
            " ORDER BY f.rate DESC";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, rating) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_GENRES_QUERY = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating = ? WHERE id = ?";

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
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> insert(INSERT_GENRES_QUERY, film.getId(), genre.getId()));
        }
        return film;
    }

    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return jdbc.query(GET_COMMON_FILMS, mapper, userId, friendId);
    }

}