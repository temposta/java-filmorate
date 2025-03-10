package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    private final ObjectMapper mapper;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        final Set<Genre> genres = setFromJsonArray(resultSet.getString("genres"));

        final Set<Director> directors = setFromJsonArray(resultSet.getString("directors"));

        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .mpa(Mpa.builder()
                        .id(resultSet.getLong("rating"))
                        .name(resultSet.getString("mpa_name"))
                        .build())
                .genres(genres)
                .directors(directors)
                .build();
    }

    private <T> Set<T> setFromJsonArray(String jsonArray) {

        Set<T> result = new LinkedHashSet<>();

        if (jsonArray == null || jsonArray.isEmpty()) {
            return result;
        }
        try {
            result.addAll(mapper.readValue(jsonArray, new TypeReference<LinkedHashSet<T>>() {
            }));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}