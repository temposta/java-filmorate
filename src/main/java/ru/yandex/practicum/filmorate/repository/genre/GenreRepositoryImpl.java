package ru.yandex.practicum.filmorate.repository.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
        return jdbc.getJdbcOperations().query(sql,
                (rs, rowNum) -> Genre.builder()
                        .id(rs.getLong("GENRE_ID"))
                        .name(rs.getString("NAME").trim())
                        .build());
    }

    @Override
    public Genre findById(long id) {
        String sql = """
                SELECT GENRE_ID, NAME
                FROM GENRES
                WHERE GENRE_ID = :id;""";
        return jdbc.queryForObject(sql, new MapSqlParameterSource("id", id),
                (rs, rowNum) -> Genre.builder()
                        .id(rs.getLong("GENRE_ID"))
                        .name(rs.getString("NAME").trim())
                        .build());
    }
}
