package ru.yandex.practicum.filmorate.dal.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.dal.repository.LikeRepository;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    final LikeRepository likeRepository;

    @Override
    public List<Like> findAll() {
        return likeRepository.findAll();
    }

    @Override
    public void create(Long userId, Long filmId) {
        likeRepository.create(userId, filmId);
    }

    @Override
    public void delete(Long userId, Long filmId) {
        likeRepository.remove(userId, filmId);
    }

}