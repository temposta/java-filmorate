package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FilmGenresMapExtractor implements ResultSetExtractor<Map<Long, Set<String>>> {
    @Override
    public Map<Long, Set<String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Set<String>> filmGenresMap = new HashMap<>();
        Set<String> filmGenresSet;
        while (rs.next()) {
            Long filmId = rs.getLong("film_id");
            filmGenresSet = filmGenresMap.get(filmId);
            if (filmGenresSet == null) {
                filmGenresSet = new HashSet<>();
                filmGenresSet.add(rs.getString("genre"));
                filmGenresMap.put(filmId, filmGenresSet);
            } else {
                filmGenresSet.add(rs.getString("genre"));
            }
        }
        return filmGenresMap;
    }
}
