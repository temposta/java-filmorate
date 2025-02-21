package ru.yandex.practicum.filmorate.repository.film.friend;

import java.util.Map;
import java.util.Set;

public interface FriendRepository {

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    Set<Long> getFriendIds(Long userId);

    Set<Long> getCommonFriendIds(Long user1, Long user2);

    Map<Long, Set<Long>> getAllFriendSets();
}
