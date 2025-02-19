package ru.yandex.practicum.filmorate.repository.film.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LikeRepositoryImplTest {

    DataSource dataSource;
    NamedParameterJdbcOperations jdbc;
    LikeRepository repository;

    public LikeRepositoryImplTest() {
        dataSource = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("classpath:/schema.sql")
                .addScript("classpath:/test-data.sql")
                .build();
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.repository = new LikeRepositoryImpl(jdbc);
    }

    @Test
    @DisplayName("добавление лайка без ошибки")
    void addLike() {
        assertDoesNotThrow(() -> repository.addLike(5L,5L));
    }

    @Test
    @DisplayName("добавление лайка с неверным film_id")
    void addLikeWithIncorrectFilmId() {
        assertThrows(DataIntegrityViolationException.class, () -> repository.addLike(555L, 5L));
    }

    @Test
    @DisplayName("добавление лайка с неверным user_id")
    void addLikeWithIncorrectUserId() {
        assertThrows(DataIntegrityViolationException.class, () -> repository.addLike(5L, 555L));
    }

    @Test
    @DisplayName("удаление лайка без ошибки")
    void removeLike() {
        assertDoesNotThrow(() -> repository.removeLike(2L,2L));
    }
}