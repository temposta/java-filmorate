package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

@Repository
public class DirectorRepository extends BaseRepository<Director> {
    private static final String FIND_ALL_QUERY = """
            SELECT * FROM director""";
    private static final String FIND_BY_ID_QUERY = """
            SELECT * FROM director WHERE id = ?""";
    private static final String INSERT_QUERY = """
            INSERT INTO director (name) VALUES (?)""";
    private static final String UPDATE_QUERY = """
            UPDATE director SET name=? WHERE id=?""";
    private static final String DELETE_QUERY = """
            DELETE FROM director WHERE id = ?
            """;

    public DirectorRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public List<Director> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Director> findById(Long directorId) {
        return findOne(FIND_BY_ID_QUERY, directorId);
    }

    public Director create(Director director) {
        Long directorId = insertWithGeneratedId(INSERT_QUERY, director.getName());
        director.setId(directorId);
        return director;
    }

    public Director update(Director director) {
        update(UPDATE_QUERY, director.getName(), director.getId());
        return director;
    }

    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }
}