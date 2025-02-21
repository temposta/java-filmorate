package ru.yandex.practicum.filmorate.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Primary
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcOperations jdbc;

    public UserRepositoryImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        log.info("UserRepositoryImpl created");
    }

    @Override
    public User add(User entity) {
        String sql = """
                INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTH_DATE)
                VALUES ( :email, :login, :name, :birthDate );
                """;
        MapSqlParameterSource params = getMapSqlParameterSourceFromUser(entity);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, params, keyHolder);
        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return entity;
    }

    @Override
    public User update(User entity) {
        String sql = """
                UPDATE USERS SET
                EMAIL = :email,
                LOGIN = :login,
                NAME = :name,
                BIRTH_DATE = :birthDate
                WHERE USER_ID = :userId;
                """;
        final MapSqlParameterSource params = getMapSqlParameterSourceFromUser(entity);
        jdbc.update(sql, params);
        return entity;
    }

    @Override
    public User delete(User entity) {
        String sql = """
                DELETE FROM USERS WHERE USER_ID = :userId;
                """;
        jdbc.update(sql, new MapSqlParameterSource()
                .addValue("userId", entity.getId()));
        return entity;
    }

    @Override
    public List<User> getAll() {
        String sql = """
                SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTH_DATE
                FROM USERS;
                """;
        return jdbc.query(sql, getUserRowMapper());
    }

    @Override
    public User findById(long id) {
        String sql = """
                SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTH_DATE
                FROM USERS
                WHERE USER_ID = :userId;
                """;

        return jdbc.queryForObject(sql,
                new MapSqlParameterSource().addValue("userId", id),
                getUserRowMapper());
    }

    @Override
    public void checkUser(Long id) {
        String sql = """
                SELECT USER_ID FROM USERS WHERE USER_ID = :id;
                """;
        Integer i = jdbc.query(sql,
                new MapSqlParameterSource().addValue("id", id),
                rs -> {
                    int count = 0;
                    while (rs.next()) count++;
                    return count;
                });
        if (i == 0) throw new EmptyResultDataAccessException("User with userId " + id + " not found", 1);
    }

    @Override
    public List<User> getListUsers(Set<Long> ids) {
        String idsString = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTH_DATE FROM USERS WHERE USER_ID in (" +
                     idsString + ");";
        List<User> users;
        users = jdbc.query(sql, getUserRowMapper());
        return users;
    }

    private static RowMapper<User> getUserRowMapper() {
        return (rs, rowNum) -> User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTH_DATE").toLocalDate())
                .build();
    }

    private static MapSqlParameterSource getMapSqlParameterSourceFromUser(User entity) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", entity.getEmail());
        params.addValue("login", entity.getLogin());
        String name = entity.getName();
        if (name != null) params.addValue("name", name);
        else {
            params.addValue("name", entity.getLogin());
            entity.setName(entity.getLogin());
        }
        params.addValue("birthDate", entity.getBirthday());
        Long id = entity.getId();
        if (id != null) params.addValue("userId", id);
        return params;
    }
}
