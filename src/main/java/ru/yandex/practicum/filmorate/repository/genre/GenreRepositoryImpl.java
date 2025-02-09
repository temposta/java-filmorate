package ru.yandex.practicum.filmorate.repository.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@AllArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {

    NamedParameterJdbcOperations jdbc;

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM GENRES";
        return jdbc.getJdbcOperations().query(sql, new BeanPropertyRowMapper<>(Genre.class));
    }

    @Override
    public Genre findById(long id) {
        String sql = "SELECT * FROM GENRES WHERE ID = :id";
        return jdbc.getJdbcOperations().queryForObject(sql, new BeanPropertyRowMapper<>(Genre.class), id);
    }
}
