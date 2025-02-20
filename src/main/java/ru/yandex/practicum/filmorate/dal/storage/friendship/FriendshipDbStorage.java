package ru.yandex.practicum.filmorate.dal.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.dal.repository.FriendshipRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    final FriendshipRepository friendshipRepository;

    @Override
    public List<Friendship> getAll() {
        return friendshipRepository.findAll();
    }

    @Override
    public void create(Long userId, Long friendId) {
        friendshipRepository.create(userId, friendId);
    }

    @Override
    public void delete(Long userId, Long friendId) {
        friendshipRepository.delete(userId, friendId);
    }

}