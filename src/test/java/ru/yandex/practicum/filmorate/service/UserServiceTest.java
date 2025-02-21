package ru.yandex.practicum.filmorate.service;

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
import ru.yandex.practicum.filmorate.repository.film.friend.FriendRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.user.UserRepositoryImpl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"classpath:schema.sql", "classpath:test-data.sql"})
@Import({UserService.class, UserRepositoryImpl.class, FriendRepositoryImpl.class})
@DisplayName("тестирование UserService")
class UserServiceTest {

    @Autowired
    private UserService userService;
    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("UserName")
                .email("useremail@useremail.mail")
                .login("user555")
                .birthday(LocalDate.parse("1980-05-18"))
                .build();
    }

    @Test
    @DisplayName("добавление пользователя")
    void createUser() {
        User createdUser = userService.createUser(user);
        assertThat(createdUser)
                .isNotNull()
                .isEqualTo(user);
        System.out.println("createdUser = " + createdUser);
    }

    @Test
    @DisplayName("обновление пользователя")
    void updateUser() {
        user.setId(2L);
        User updatedUser = userService.updateUser(user);
        assertThat(updatedUser)
                .isNotNull()
                .isEqualTo(user);
    }

    @Test
    @DisplayName("получение списка всех пользователей")
    void getAllUsers() {
        List<User> users = userService.getAllUsers();
        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(6);
    }

    @Test
    @DisplayName("удаление пользователя без ошибки")
    void deleteUser() {
        user.setId(2L);
        assertDoesNotThrow(() -> {
            userService.deleteUser(user);
        });
        assertThrows(EmptyResultDataAccessException.class, () -> userService.getUserById(2L));
    }

    @Test
    @DisplayName("получение пользователя по id")
    void getUserById() {
        User user = userService.getUserById(2L);
        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("login", "test1")
                .hasFieldOrPropertyWithValue("name", "test1")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.parse("2010-11-07"))
                .hasFieldOrPropertyWithValue("email", "test1@test.ru");
    }

    @Test
    @DisplayName("добавление в друзья")
    void addFriend() {
        assertDoesNotThrow(() -> userService.addFriend(2L, 4L));
    }

    @Test
    @DisplayName("удаление из друзей без ошибки")
    void deleteFriend() {
        assertDoesNotThrow(() -> userService.deleteFriend(2L, 3L));
    }

    @Test
    @DisplayName("получение списка друзей")
    void getFriends() {
        List<User> friends = userService.getFriends(2L)
                .stream()
                .sorted(Comparator.comparingLong(User::getId))
                .toList();
        assertThat(friends)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        assertEquals(friends.get(0).getId(), 3L);
        assertEquals(friends.get(1).getId(), 6L);
    }

    @Test
    @DisplayName("получение списка общих друзей")
    void getFriendsCommon() {
        List<User> friends = userService.getFriendsCommon(2L, 5L)
                .stream()
                .sorted(Comparator.comparingLong(User::getId))
                .toList();
        assertThat(friends)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        assertEquals(friends.get(0).getId(), 3L);
        assertEquals(friends.get(1).getId(), 6L);
    }
}