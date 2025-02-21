package ru.yandex.practicum.filmorate.repository.film.like;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    NamedParameterJdbcOperations jdbc;

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO LIKES VALUES (:filmId, :userId)";
        SqlParameterSource params = new MapSqlParameterSource("filmId", filmId)
                .addValue("userId", userId);
        int rowsAffected = jdbc.update(sql, params);
        log.info("Added like to database, the number of rows affected: {}", rowsAffected);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM LIKES WHERE FILM_ID = :filmId AND USER_ID = :userId";
        SqlParameterSource params = new MapSqlParameterSource("filmId", filmId)
                .addValue("userId", userId);
        int rowsAffected = jdbc.update(sql, params);
        log.info("Remove like from database, the number of rows affected: {}", rowsAffected);
    }
}
