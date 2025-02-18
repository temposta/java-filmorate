package ru.yandex.practicum.filmorate.dal.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {

    List<Friendship> getAll();

    void create(long userId, long friendId);

    void delete(long userId, long friendId);

}