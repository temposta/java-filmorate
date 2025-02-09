package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@AllArgsConstructor
public class MpaRepositoryImpl implements MpaRepository {

    NamedParameterJdbcOperations jdbc;

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT MPA_ID, NAME FROM MPARATINGS";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Mpa.class));
    }

    @Override
    public Mpa findById(long id) {
        String sql = "SELECT MPA_ID, NAME FROM MPARATINGS WHERE MPA_ID = :id";
        return jdbc.getJdbcOperations().queryForObject(sql, new BeanPropertyRowMapper<>(Mpa.class), id);
    }
}
