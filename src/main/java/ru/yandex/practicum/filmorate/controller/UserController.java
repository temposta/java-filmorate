package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение списка всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user.toString());

        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))){
            log.error("Адрес электронной почты {} уже используется", user.getEmail());
            throw new ValidationException("Этот адрес электронной почты уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()){
            log.warn("Имя пользователя не указано, используется логин ({}) в качестве имени", user.getLogin());
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен с идентификатором {}", user.getId());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            log.error("Для изменяемого пользователя не указан идентификатор во входящих аргументах");
            throw new ValidationException("Id должен быть указан");
        }

        if (newUser.getEmail() == null || newUser.getName() == null || newUser.getLogin() == null){
            log.warn("В составе аргументов изменяемого пользователя не заполнены обязательные значения, изменение не выполнено");
            return newUser;
        }

        if (users.containsKey(newUser.getId())) {
            if (users.values().stream()
                    .anyMatch(u -> u.getEmail().equals(newUser.getEmail()) && !Objects.equals(u.getId(), newUser.getId()))){
                log.error("Новый адрес электронной почты {} уже используется для другого пользователя", newUser.getEmail());
                throw new ValidationException("Этот адрес электронной почты уже используется");
            }

            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            User oldUser = newUser.toBuilder().build();
            users.put(newUser.getId(), oldUser);
            return oldUser;
        }

        log.error("Пользователь с id = {} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}