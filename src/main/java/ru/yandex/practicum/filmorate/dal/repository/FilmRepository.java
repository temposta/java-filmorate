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
            SELECT f.*, mpa.name as mpa_name, d.directors as directors, g.genres as genres
            FROM film f
            LEFT JOIN (
                SELECT fg.film_id, json_arrayagg(json_object('id' : g.id, 'name' : g.name) ORDER BY g.id) AS genres
                FROM film_genre fg
                LEFT JOIN genre g on g.ID = fg.genre_id
                GROUP BY fg.film_id) g on f.ID = g.film_id
            LEFT JOIN (
                SELECT fd.film_id, json_arrayagg(json_object('id' : d.id, 'name' :   d.name)) AS directors
                FROM film_director fd
                LEFT JOIN director d on d.id = fd.director_id
                GROUP BY fd.film_id) d ON f.id = d.film_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            WHERE f.id = ?
            GROUP BY f.id""";
    private static final String FIND_ALL_QUERY = """
            SELECT f.*, mpa.name as mpa_name, d.directors as directors, g.genres as genres
            FROM film f
            LEFT JOIN (
                SELECT fg.film_id, json_arrayagg(json_object('id' : g.id, 'name' : g.name) ORDER BY g.id) AS genres
                FROM film_genre fg
                LEFT JOIN genre g on g.ID = fg.genre_id
                GROUP BY fg.film_id) g on f.ID = g.film_id
            LEFT JOIN (
                SELECT fd.film_id, json_arrayagg(json_object('id' : d.id, 'name' :   d.name)) AS directors
                FROM film_director fd
                LEFT JOIN director d on d.id = fd.director_id
                GROUP BY fd.film_id) d ON f.id = d.film_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            GROUP BY f.id""";
    private static final String FIND_POPULAR_QUERY = """
            SELECT f.*, mpa.name as mpa_name, d.directors as directors, g.genres as genres
            FROM film f
            LEFT JOIN (
                SELECT fg.film_id, json_arrayagg(json_object('id' : g.id, 'name' : g.name) ORDER BY g.id) AS genres
                FROM film_genre fg
                LEFT JOIN genre g on g.ID = fg.genre_id
                GROUP BY fg.film_id) g on f.ID = g.film_id
            LEFT JOIN (
                SELECT fd.film_id, json_arrayagg(json_object('id' : d.id, 'name' :   d.name)) AS directors
                FROM film_director fd
                LEFT JOIN director d on d.id = fd.director_id
                GROUP BY fd.film_id) d ON f.id = d.film_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN likes l on l.film_id = f.id
            GROUP BY f.id
            ORDER BY count(l.user_id) DESC limit ?""";
    private static final String INSERT_QUERY = """
            INSERT INTO film (name, description, release_date, duration, rating)
            VALUES (?, ?, ?, ?, ?)""";
    private static final String DELETE_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating = ? WHERE id = ?";
    private static final String SEARCH_BY_QUERY = """
            SELECT f.*, mpa.name as mpa_name, d.directors as directors, g.genres as genres
            FROM film f
            LEFT JOIN (
                SELECT fg.film_id, json_arrayagg(json_object('id' : g.id, 'name' : g.name) ORDER BY g.id) AS genres
                FROM film_genre fg
                LEFT JOIN genre g on g.ID = fg.genre_id
                GROUP BY fg.film_id) g on f.ID = g.film_id
            LEFT JOIN (
                SELECT fd.film_id, json_arrayagg(json_object('id' : d.id, 'name' :   d.name)) AS directors
                FROM film_director fd
                LEFT JOIN director d on d.id = fd.director_id
                GROUP BY fd.film_id) d ON f.id = d.film_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN likes l on l.film_id = f.id
            WHERE f.name ILIKE ? OR directors ILIKE ?
            GROUP BY f.id
            ORDER BY count(l.user_id) DESC""";
    private static final String INSERT_DIRECTORS_QUERY = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
    private static final String SEARCH_BY_DIR_YEAR_SORT = """
            SELECT f.*, mpa.name as mpa_name, d.directors as directors, g.genres as genres
            FROM film f
            LEFT JOIN (
                SELECT fg.film_id, json_arrayagg(json_object('id' : g.id, 'name' : g.name) ORDER BY g.id) AS genres
                FROM film_genre fg
                LEFT JOIN genre g on g.ID = fg.genre_id
                GROUP BY fg.film_id) g on f.ID = g.film_id
            LEFT JOIN (
                SELECT fd.film_id, json_arrayagg(json_object('id' : d.id, 'name' :   d.name)) AS directors
                FROM film_director fd
                LEFT JOIN director d on d.id = fd.director_id
                GROUP BY fd.film_id) d ON f.id = d.film_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN likes l on l.film_id = f.id
            WHERE f.id in (
                    SELECT DISTINCT fd.film_id
                    FROM film_director fd
                    WHERE fd.director_id = ?)
            GROUP BY f.id, f.release_date
            ORDER BY extract(YEAR FROM f.release_date)
            """;
    private static final String SEARCH_BY_DIR_LIKES_SORT = """
            SELECT f.*, mpa.name as mpa_name, d.directors as directors, g.genres as genres
            FROM film f
            LEFT JOIN (
                SELECT fg.film_id, json_arrayagg(json_object('id' : g.id, 'name' : g.name) ORDER BY g.id) AS genres
                FROM film_genre fg
                LEFT JOIN genre g on g.ID = fg.genre_id
                GROUP BY fg.film_id) g on f.ID = g.film_id
            LEFT JOIN (
                SELECT fd.film_id, json_arrayagg(json_object('id' : d.id, 'name' :   d.name)) AS directors
                FROM film_director fd
                LEFT JOIN director d on d.id = fd.director_id
                GROUP BY fd.film_id) d ON f.id = d.film_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN likes l on l.film_id = f.id
            WHERE f.id in (
                    SELECT DISTINCT fd.film_id
                    FROM film_director fd
                    WHERE fd.director_id = ?)
            GROUP BY f.id
            ORDER BY count(l.user_id) DESC
            """;
    private static final String DELETE_GENRES_DIRECTORS_QUERY = "DELETE FROM film_genre WHERE film_id = ?;\n" +
                                                                "DELETE FROM film_director WHERE film_id = ?;";
    private static final String GET_COMMON_FILMS = """
            SELECT f.*, mpa.name as mpa_name, d.directors as directors, g.genres as genres
            FROM film f
            LEFT JOIN (
                SELECT fg.film_id, json_arrayagg(json_object('id' : g.id, 'name' : g.name) ORDER BY g.id) AS genres
                FROM film_genre fg
                LEFT JOIN genre g on g.ID = fg.genre_id
                GROUP BY fg.film_id) g on f.ID = g.film_id
            LEFT JOIN (
                SELECT fd.film_id, json_arrayagg(json_object('id' : d.id, 'name' :   d.name)) AS directors
                FROM film_director fd
                LEFT JOIN director d on d.id = fd.director_id
                GROUP BY fd.film_id) d ON f.id = d.film_id
            LEFT JOIN rating mpa on mpa.id = f.rating
            LEFT JOIN likes l on l.film_id = f.id
            WHERE f.id in (
                    SELECT film_id
                    FROM likes
                    WHERE user_id IN (?, ?)
                    GROUP BY film_id
                    HAVING count(film_id) > 1)
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
        String titlePattern = by.contains(SearchValues.TITLE) ? "%" + query + "%" : "";
        String directorPattern = by.contains(SearchValues.DIRECTOR) ? "%" + query + "%" : "";
        return findMany(SEARCH_BY_QUERY, titlePattern, directorPattern);
    }

    public List<Film> search(Long directorId, SortValue sortValue) {
        return findMany(
                switch (sortValue) {
                    case YEAR -> SEARCH_BY_DIR_YEAR_SORT;
                    case LIKES -> SEARCH_BY_DIR_LIKES_SORT;
                },
                directorId);
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