package ru.yandex.practicum.filmorate.repository.mpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MpaRepositoryImplTest {

    DataSource dataSource;

    NamedParameterJdbcOperations jdbc;
    MpaRepository repository;

    public MpaRepositoryImplTest() {
        dataSource = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("classpath:/schema.sql")
                .addScript("classpath:/test-data.sql")
                .build();
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.repository = new MpaRepositoryImpl(jdbc);
    }

    @Test
    @DisplayName("получение списка рейтингов")
    void getAll() {
        List<Mpa> mpaList = repository.getAll();
        mpaList.forEach(System.out::println);
        assertNotNull(mpaList);
        assertEquals(5, mpaList.size());
    }

    @Test
    @DisplayName("получение рейтинга по идентификатору")
    void findById() {
        Mpa mpa = repository.findById(1L);
        System.out.println("mpa = " + mpa);
        assertNotNull(mpa);
        assertEquals(1L, mpa.getId());
        assertEquals("G", mpa.getName());
    }
}