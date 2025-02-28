package ru.yandex.practicum.filmorate.dal.storage.like;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {

    List<Like> findAll();

    void create(Long userId, Long filmId);

    void delete(Long userId, Long filmId);

}