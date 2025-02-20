package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user.toString());
        return userService.create(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User read(@PathVariable("id") Long id) {
        log.info("Получен запрос на получение пользователя с идентификатором: {}", id);
        return userService.read(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser.toString());
        return userService.update(newUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление фильма с пользователя: {}", id);
        userService.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAll() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable("id") Long userId) {
        log.info("Вызван метод GET /users/{id}/friends с id = {}", userId);
        List<User> userFriends = userService.getFriends(userId);
        log.info("Метод GET /users/{id}/friends вернул ответ {}", userFriends);
        return userFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriendsCommonOther(@PathVariable("id") Long userId,
                                            @PathVariable("otherId") Long otherId) {
        log.info("Вызван метод GET /users/{id}/friends/common/{otherId} с id = {} и otherId = {}", userId, otherId);
        List<User> userFriends = userService.getFriendsCommonOther(userId, otherId);
        log.info("Метод GET /users/{id}/friends/common/{otherId} вернул ответ {}", userFriends);
        return userFriends;
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") Long userId,
                                @PathVariable("friendId") Long friendId) {
        log.info("Вызван метод PUT /{id}/friends/{friendId} с id = {} и friendId = {}", userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("Метод PUT /{id}/friends/{friendId} успешно выполнен");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable("id") Long userId,
                             @PathVariable("friendId") Long friendId) {
        log.info("Вызван метод DELETE /{id}/friends/{friendId} с id = {} и friendId = {}", userId, friendId);
        userService.removeFriend(userId, friendId);
        log.info("Метод DELETE /{id}/friends/{friendId} успешно выполнен");
    }

}