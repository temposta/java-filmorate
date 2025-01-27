package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private long sequenceId;
    private final Map<Long, User> users;
    private final Map<Long, Set<User>> friends;

    public InMemoryUserStorage() {
        sequenceId = 0L;
        users = new HashMap<>();
        friends = new HashMap<>();
    }

    @Override
    public User create(User user) {

        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.error("Адрес электронной почты {} уже используется", user.getEmail());
            throw new ValidationException("Этот адрес электронной почты уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не указано, используется логин ({}) в качестве имени", user.getLogin());
            user.setName(user.getLogin());
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public User read(Long userId) {
        return users.get(userId);
    }

    @Override
    public User update(User user) throws NotFoundException {

        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, user.getId()));
        }

        if (users.values().stream()
                .anyMatch(u -> !Objects.equals(u.getId(), user.getId()) && u.getEmail().equals(user.getEmail()))) {
            log.error("Адрес электронной почты {} уже используется", user.getEmail());
            throw new ValidationException("Этот адрес электронной почты уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не указано, используется логин ({}) в качестве имени", user.getLogin());
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
        friends.remove(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> getFriends(User user) {
        return new ArrayList<>(friends.get(user.getId()));
    }

    @Override
    public List<User> getFriendsCommonOther(User user, User otherUser) {
        final Set<User> userFriends = friends.get(user.getId());
        final Set<User> otherUserFriends = friends.get(otherUser.getId());

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .toList();
    }

    @Override
    public List<User> addFriend(User user, User friend) {
        Set<User> userFriends = friends.get(friend.getId());
        userFriends.add(user);

        userFriends = friends.get(user.getId());
        userFriends.add(friend);

        return new ArrayList<>(userFriends);
    }

    @Override
    public void removeFriend(User user, User friend) {
        Set<User> userFriends = friends.get(friend.getId());
        userFriends.remove(user);

        userFriends = friends.get(user.getId());
        userFriends.remove(friend);
    }

    private long generateId() {
        log.info("Сгенерирован id = {} для нового пользователя", ++sequenceId);
        return sequenceId;
    }

}