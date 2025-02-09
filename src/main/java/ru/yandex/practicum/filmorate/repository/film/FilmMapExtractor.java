package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FilmMapExtractor implements ResultSetExtractor<Map<Long, Film>> {

    @Override
    public Map<Long, Film> extractData(ResultSet rs) throws DataAccessException, SQLException {
        Map<Long, Film> filmMap = new HashMap<>();
        Film film;
        while (rs.next()) {
            film = Film.builder()
                    .id(rs.getLong("ID"))
                    .name(rs.getString("F_NAME"))
                    .description(rs.getString("DESCRIPTION"))
                    .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                    .duration(rs.getInt("DURATION"))
                    .mpa(Mpa.builder()
                            .id(rs.getLong("MPA_ID"))
                            .name(rs.getString("M_NAME").trim())
                            .build())
                    .build();
            filmMap.put(film.getId(), film);
        }
        return filmMap;
    }
}
