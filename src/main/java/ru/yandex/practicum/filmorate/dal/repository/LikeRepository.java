package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

@Repository
public class LikeRepository extends BaseRepository<Like> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM likes";
    private static final String INSERT_QUERY = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";

    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public List<Like> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public void create(Long userId, Long filmId) {
        insert(INSERT_QUERY, userId, filmId);
    }

    public void remove(Long userId, Long filmId) {
        delete(DELETE_QUERY, userId, filmId);
    }
}
