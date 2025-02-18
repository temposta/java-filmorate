package ru.yandex.practicum.filmorate.dal.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.dal.repository.FriendshipRepository;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    final FriendshipRepository friendshipRepository;

    @Override
    public List<Friendship> getAll() {
        return friendshipRepository.findAll();
    }

    @Override
    public void create(long userId, long friendId) {
        friendshipRepository.create(userId, friendId);
    }

    @Override
    public void delete(long userId, long friendId) {
        friendshipRepository.delete(userId, friendId);
    }

}