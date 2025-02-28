package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingRepository extends BaseRepository<Mpa> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE id = ?";

    public RatingRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Mpa> findById(Long ratingId) {
        return findOne(FIND_BY_ID_QUERY, ratingId);
    }

}