package ru.yandex.practicum.filmorate.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Objects;

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
        params.addValue("userId", entity.getId());
        jdbc.update(sql, params);
        return entity;
    }

    @Override
    public User delete(User entity) {
        String sql = """
                DELETE FROM USERS WHERE USER_ID = :userId;
                """;
        jdbc.update(sql, new MapSqlParameterSource().addValue("userId", entity.getId()));
        return entity;
    }

    @Override
    public List<User> getAll() {
        String sql = """
                SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTH_DATE
                FROM USERS;
                """;
        return jdbc.query(sql, (rs, rowNum) -> User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTH_DATE").toLocalDate())
                .build());
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
                (rs, rowNum) -> User.builder()
                        .id(rs.getLong("USER_ID"))
                        .email(rs.getString("EMAIL"))
                        .login(rs.getString("LOGIN"))
                        .name(rs.getString("NAME"))
                        .birthday(rs.getDate("BIRTH_DATE").toLocalDate())
                        .build());
    }
}
