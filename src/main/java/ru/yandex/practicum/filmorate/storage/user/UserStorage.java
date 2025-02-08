package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User read(Long userId);

    User update(User user);

    void delete(Long userId);

    List<User> getAll();

    List<User> getFriends(User user);

    List<User> getFriendsCommonOther(User user, User otherUser);

    List<User> addFriend(User user, User friend);

    void removeFriend(User user, User friend);

}