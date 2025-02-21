package ru.yandex.practicum.filmorate.repository.film.like;

public interface LikeRepository {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}
