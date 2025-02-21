package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FilmGenresMapExtractor implements ResultSetExtractor<Map<Long, Set<Genre>>> {
    @Override
    public Map<Long, Set<Genre>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Set<Genre>> filmGenresMap = new HashMap<>();
        Set<Genre> filmGenresSet;
        while (rs.next()) {
            Long filmId = rs.getLong("film_id");
            filmGenresSet = filmGenresMap.get(filmId);
            if (filmGenresSet == null) {
                filmGenresSet = new HashSet<>();
                filmGenresSet.add(Genre.builder()
                        .id(rs.getLong("genre_id"))
                        .name(rs.getString("name").trim())
                        .build());
                filmGenresMap.put(filmId, filmGenresSet);
            } else {
                filmGenresSet.add(Genre.builder()
                        .id(rs.getLong("genre_id"))
                        .name(rs.getString("name").trim())
                        .build());
            }
        }
        return filmGenresMap;
    }
}
