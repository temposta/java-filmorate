package ru.yandex.practicum.filmorate.repository.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("тестирование GenreRepository")
class GenreRepositoryImplTest {

    DataSource dataSource;
    NamedParameterJdbcOperations jdbc;
    GenreRepository repository;

    public GenreRepositoryImplTest() {
        dataSource = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("classpath:/schema.sql")
                .addScript("classpath:/test-data.sql")
                .build();
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.repository = new GenreRepositoryImpl(jdbc);
    }

    @Test
    @DisplayName("получение списка жанров")
    void getAll() {
        List<Genre> genreList = repository.getAll();
        genreList.forEach(System.out::println);
        assertNotNull(genreList);
        assertEquals(6, genreList.size());
    }

    @Test
    @DisplayName("получение жанра по идентификатору")
    void findById() {
        Genre genre = repository.findById(1L);
        System.out.println("genre = " + genre);
        assertNotNull(genre);
        assertEquals(1L, genre.getId());
        assertEquals("Комедия", genre.getName());
    }
}