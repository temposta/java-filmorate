package ru.yandex.practicum.filmorate.repository.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
@Primary
@AllArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    NamedParameterJdbcOperations jdbc;

    @Override
    public Film add(Film entity) {
        String sql = """
                INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
                VALUES (:name, :description, :releaseDate, :duration,
                    (SELECT MPA_ID"
                       FROM MPARATINGS"
                       WHERE TRIM(MPARATINGS.NAME) = :mpa_name));""";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("releaseDate", entity.getReleaseDate())
                .addValue("duration", entity.getDuration())
                .addValue("mpa_name", entity.getMpa());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, params, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        entity.setId(id);
        Set<String> genres = entity.getGenres();
        updateGenresOnFilmId(genres, id);
        return entity;
    }

    @Override
    public Film update(Film entity) {
        Long id = entity.getId();
        String sql = """
                UPDATE FILMS SET
                NAME = :name,
                DESCRIPTION = :description,
                RELEASE_DATE = :releaseDate,
                DURATION = :duration,
                MPA_ID = (SELECT MPA_ID
                    FROM MPARATINGS
                    WHERE TRIM(MPARATINGS.NAME) = :mpa_name)
                WHERE FILM_ID = :id;""";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("releaseDate", entity.getReleaseDate())
                .addValue("duration", entity.getDuration())
                .addValue("mpa_name", entity.getMpa());
        jdbc.update(sql, params);
        String sqlDeleteGenres = """
                DELETE FROM FILMGENRES WHERE FILM_ID =:id;""";
        jdbc.getJdbcOperations().update(sqlDeleteGenres,
                new MapSqlParameterSource().addValue("id", id));
        Set<String> genres = entity.getGenres();
        updateGenresOnFilmId(genres, id);
        return entity;
    }

    @Override
    public Film delete(Film entity) {
        Long id = entity.getId();
        String sql = """
                DELETE FROM FILMS WHERE FILM_ID =:id;""";
        jdbc.update(sql, new MapSqlParameterSource().addValue("id", id));
        return entity;
    }

    @Override
    public List<Film> getAll() {
        final String sqlGetFilms = """
                SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, RELEASE_DATE,
                   DURATION, M.NAME, COUNT(L.USER_ID) AS LIKES
                FROM FILMS AS F
                LEFT JOIN MPARATINGS M on F.MPA_ID = M.MPA_ID
                LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID
                GROUP BY F.FILM_ID;""";
        Optional<Map<Long, Film>> films = Optional.ofNullable(jdbc.query(sqlGetFilms, new FilmMapExtractor()));

        if (films.isEmpty()) return List.of();

        final String sqlGetFilmGenres = """
                SELECT FILM_ID, NAME
                FROM FILMGENRES AS FG
                         LEFT JOIN PUBLIC.GENRES G on FG.GENRE_ID = G.GENRE_ID;
                """;
        Optional<Map<Long, Set<String>>> genres = Optional.ofNullable(jdbc.query(sqlGetFilmGenres, new FilmGenresMapExtractor()));

        if (genres.isEmpty()) return new ArrayList<>(films.get().values());
        Map<Long, Film> currentFilms = films.get();
        Map<Long, Set<String>> currentGenres = genres.get();
        for (Long filmId : currentGenres.keySet()) {
            Film film = currentFilms.get(filmId);
            film.setGenres(currentGenres.get(filmId));
        }
        return new ArrayList<>(currentFilms.values());
    }

    @Override
    public Film findById(long id) {
        final String sqlGetFilms = """
                SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, RELEASE_DATE,
                   DURATION, M.NAME, COUNT(L.USER_ID) AS LIKES
                FROM FILMS AS F
                WHERE F.FILM_ID = :id
                LEFT JOIN MPARATINGS M on F.MPA_ID = M.MPA_ID
                LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID
                GROUP BY F.FILM_ID;""";
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        Optional<Map<Long, Film>> filmOpt = Optional.ofNullable(jdbc.query(sqlGetFilms, params, new FilmMapExtractor()));
        Film film = filmOpt.map(filmMap -> filmMap.get(id)).orElse(null);
        if (film == null) return null;
        final String sqlGetFilmGenres = """
                SELECT NAME
                FROM FILMGENRES AS FG
                WHERE FG.FILM_ID = :id
                         LEFT JOIN PUBLIC.GENRES G on FG.GENRE_ID = G.GENRE_ID;
                """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        jdbc.query(sqlGetFilmGenres, param, rs -> {
            while (rs.next()) film.getGenres().add(rs.getString("NAME"));
        });
        return film;
    }

    private void updateGenresOnFilmId(Set<String> genres, Long id) {
        if (!genres.isEmpty()) {
            String sqlGenres = """
                    INSERT INTO FILMGENRES (FILM_ID, GENRE_ID)
                    VALUES (:id , (
                        SELECT GENRE_ID FROM GENRES
                        WHERE TRIM(NAME) = :genre_name
                        ));""";

            SqlParameterSource[] parameters = new SqlParameterSource[genres.size()];
            final int[] i = {0};
            genres.forEach(genre -> {
                parameters[i[0]] = new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("genre_name", genre);
                i[0]++;
            });
            jdbc.batchUpdate(sqlGenres, parameters);
        }
    }
}
