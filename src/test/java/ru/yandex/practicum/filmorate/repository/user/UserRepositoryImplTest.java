package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"classpath:schema.sql", "classpath:test-data.sql"})
@Import({UserRepositoryImpl.class})
@DisplayName("тестирование UserRepositoryImpl")
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .login("user1")
                .email("user1@email.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

    }

    @Test
    @DisplayName("добавление User без поля name")
    void addUserWithoutName() {
        User addingUser = userRepository.add(user);
        String login = user.getLogin();

        assertNotNull(addingUser.getId());
        assertEquals(login, addingUser.getLogin());
        assertEquals(login, addingUser.getName());
    }

    @Test
    @DisplayName("Обновление User")
    void update() {
        user.setId(1L);
        User updatingUser = userRepository.update(user);
        assertEquals(updatingUser.getId(), 1L);
        assertEquals(updatingUser.getLogin(), user.getLogin());
        assertEquals(updatingUser.getName(), user.getName());
        assertEquals(updatingUser.getBirthday(), user.getBirthday());
    }

    @Test
    @DisplayName("удаление User")
    void delete() {
        user.setId(1L);
        userRepository.delete(user);
        assertThrows(EmptyResultDataAccessException.class, () -> userRepository.findById(1L));
    }

    @Test
    @DisplayName("получение списка пользователей")
    void getAll() {
        List<User> userList = userRepository.getAll();
        assertNotNull(userList);
        assertEquals(userList.size(), 6);
        System.out.println("получение списка пользователей");
        userList.forEach(System.out::println);
    }

    @Test
    @DisplayName("поиск пользователя по id")
    void findById() {
        assertNotNull(userRepository.findById(5L));
    }

    @Test
    @DisplayName("проверка существования пользователя")
    void checkUser() {
        assertThrows(EmptyResultDataAccessException.class, () -> userRepository.checkUser(555L));
        assertThrows(EmptyResultDataAccessException.class, () -> userRepository.checkUser(null));
    }

    @Test
    @DisplayName("запрос пользователей по списку id")
    void getListUsers() {
        Set<Long> ids = Set.of(2L, 3L, 4L, 5L);
        List<User> userList = userRepository.getListUsers(ids);
        assertNotNull(userList);
        assertEquals(userList.size(), 4);
        System.out.println("запрос пользователей по списку id");
        userList.forEach(System.out::println);
    }
}