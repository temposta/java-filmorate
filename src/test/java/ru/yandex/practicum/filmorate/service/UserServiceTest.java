package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    UserService userService;

    @BeforeEach
    public void init() {
        userService = new UserService(new InMemoryUserStorage());
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    public void createUser() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("test")
                .name("name")
                .birthday(LocalDate.of(2000, Month.JANUARY, 1))
                .build();

        user = userService.create(user);
        assertEquals("name", userService.read(user.getId()).getName(), "Имена не совпадают");

        user = userService.create(user.toBuilder().name(null).email("test2@yandex.ru").build());
        assertEquals("test", userService.read(user.getId()).getName(), "Имена не совпадают");
    }

    @Test
    @DisplayName("Проверка обновления пользователя")
    public void updateUser() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("test")
                .name("name")
                .birthday(LocalDate.now())
                .build();
        User newUser = userService.create(user.toBuilder().build());

        Throwable throwable = assertThrows(NotFoundException.class, () -> userService.update(user.toBuilder().id(0L).build()));
        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 0L), throwable.getMessage(), "Текст сообщения не совпадает");

        assertDoesNotThrow(() -> userService.update(newUser.toBuilder().login("login").build()));
        assertEquals("login", userService.read(newUser.getId()).getLogin(), "Не совпадают имена");
    }

}