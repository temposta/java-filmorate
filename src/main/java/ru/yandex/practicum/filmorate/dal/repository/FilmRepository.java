package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.SearchValues;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_BY_ID_QUERY = "SELECT f.*, mpa.name as mpa_name, dir.name as director_name, string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names " +
                                                   "FROM film f " +
                                                   "LEFT JOIN film_genre fg on fg.film_id = f.id " +
                                                   "LEFT JOIN genre g on g.id = fg.genre_id " +
                                                   "LEFT JOIN rating mpa on mpa.id = f.rating " +
                                                   "LEFT JOIN director dir on dir.id = f.director_id" +
                                                   "WHERE f.id = ? " +
                                                   "GROUP BY f.id";
    private static final String FIND_ALL_QUERY = "SELECT f.*, mpa.name as mpa_name, dir.name as director_name, string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names " +
                                                 "FROM film f " +
                                                 "LEFT JOIN film_genre fg on fg.film_id = f.id " +
                                                 "LEFT JOIN genre g on g.id = fg.genre_id " +
                                                 "LEFT JOIN rating mpa on mpa.id = f.rating " +
                                                 "LEFT JOIN director dir on dir.id = f.director_id" +
                                                 "GROUP BY f.id";
    private static final String FIND_POPULAR_QUERY = "SELECT f.*, mpa.name as mpa_name, dir.name as director_name, string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names " +
                                                     "FROM film f " +
                                                     "LEFT JOIN film_genre fg on fg.film_id = f.id " +
                                                     "LEFT JOIN genre g on g.id = fg.genre_id " +
                                                     "LEFT JOIN rating mpa on mpa.id = f.rating " +
                                                     "LEFT JOIN director dir on dir.id = f.director_id" +
                                                     "LEFT JOIN likes l on l.film_id = f.id " +
                                                     "GROUP BY f.id " +
                                                     "ORDER BY count(l.user_id) DESC limit ?";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, rating, director_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_GENRES_QUERY = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating = ?, director_id = ?, WHERE id = ?";
    private static final String SEARCH_BY_QUERY = "SELECT f.*, mpa.name as mpa_name, dir.name as director_name, string_agg(g.id, ', ') as genre_ids, string_agg(g.name, ', ') as genre_names " +
                                                  "FROM film f " +
                                                  "LEFT JOIN film_genre fg on fg.film_id = f.id " +
                                                  "LEFT JOIN genre g on g.id = fg.genre_id " +
                                                  "LEFT JOIN rating mpa on mpa.id = f.rating " +
                                                  "LEFT JOIN director dir on dir.id = f.director_id" +
                                                  "LEFT JOIN likes l on l.film_id = f.id " +
                                                  "WHERE f.name LIKE ? OR dir.name LIKE ? " +
                                                  "GROUP BY f.id " +
                                                  "ORDER BY count(l.user_id) DESC";

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
                film.getMpa() == null ? null : film.getMpa().getId(),
                film.getDirector() == null ? null : film.getDirector().getId());
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
                film.getDirector() == null ? null : film.getDirector().getId(),
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

    public List<Film> searchFilms(String query, List<SearchValues> by) {
        String namePattern;
        String dirPattern;
        namePattern = by.contains(SearchValues.TITLE) ? "%" + query + "%" : "%";
        dirPattern =  by.contains(SearchValues.DIRECTOR) ? "%" + query + "%" : "%";
        return findMany(SEARCH_BY_QUERY, namePattern, dirPattern);
    }
}