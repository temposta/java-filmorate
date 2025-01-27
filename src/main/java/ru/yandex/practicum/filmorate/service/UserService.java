package ru.yandex.practicum.filmorate.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User read(long id) {
        return userStorage.read(id);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public List<User> getFriends(Long userId) {
        User user = Optional.ofNullable(userStorage.read(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        return userStorage.getFriends(user);
    }

    public List<User> getFriendsCommonOther(Long userId, Long otherUserId) {
        User user = Optional.ofNullable(userStorage.read(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User otherUser = Optional.ofNullable(userStorage.read(otherUserId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, otherUserId)));
        return userStorage.getFriendsCommonOther(user, otherUser);
    }

    public List<User> addFriend(Long userId, Long friendId) {
        User user = Optional.ofNullable(userStorage.read(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User friend = Optional.ofNullable(userStorage.read(friendId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, friendId)));
        if (user.equals(friend))
            throw new ValidationException("Невозможно добавить в друзья самого себя");
        return userStorage.addFriend(user, friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = Optional.ofNullable(userStorage.read(userId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User friend = Optional.ofNullable(userStorage.read(friendId))
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, friendId)));
        userStorage.removeFriend(user, friend);
    }

}