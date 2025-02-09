package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FilmMapExtractor implements ResultSetExtractor<Map<Long, Film>> {
    @Override
    public Map<Long, Film> extractData(ResultSet rs) throws DataAccessException, SQLException {
        Map<Long, Film> filmMap = new HashMap<>();
        Film film;
        while (rs.next()){
            film = Film.builder()
                    .id(rs.getLong("F.ID"))
                    .name(rs.getString("F.NAME"))
                    .description(rs.getString("F.DESCRIPTION"))
                    .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                    .duration(rs.getInt("DURATION"))
                    .mpa("M.NAME")
                    .build();
            filmMap.put(film.getId(), film);
        }
        return filmMap;
    }
}
