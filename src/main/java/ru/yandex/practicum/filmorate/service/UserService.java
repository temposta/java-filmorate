package ru.yandex.practicum.filmorate.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    private final FriendshipStorage friendshipStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        user = userStorage.create(user);
        log.info("Пользователь создан {}", user.toString());
        return user;
    }

    public User read(Long id) {
        Optional<User> user = userStorage.read(id);
        if (user.isPresent()) {
            return user.get();
        }
        log.error(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, id));
        throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, id));
    }

    public User update(User user) {

        Optional<User> optionalUser = userStorage.read(user.getId());
        if (optionalUser.isEmpty()) {
            log.error(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, user.getId()));
            throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, user.getId()));
        }

        user = userStorage.update(user);
        log.info("Пользователь обновлен {}", user.toString());
        return user;
    }

    public void delete(Long id) {
        userStorage.delete(id);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.read(userId)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        return userStorage.getFriends(user);
    }

    public List<User> getFriendsCommonOther(Long userId, Long otherUserId) {
        User user = userStorage.read(userId)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User otherUser = userStorage.read(otherUserId)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, otherUserId)));
        return userStorage.getFriendsCommonOther(user, otherUser);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.read(userId)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User friend = userStorage.read(friendId)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, friendId)));
        if (user.equals(friend))
            throw new ValidationException("Невозможно добавить в друзья самого себя");
        friendshipStorage.create(user.getId(), friend.getId());
        log.info("Пользователь с id = {} добавил друга с id = {}", userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.read(userId)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, userId)));
        User friend = userStorage.read(friendId)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, friendId)));
        friendshipStorage.delete(user.getId(), friend.getId());
        log.info("Пользователь с id = {} удалил друга с id = {}", userId, friendId);
    }
}