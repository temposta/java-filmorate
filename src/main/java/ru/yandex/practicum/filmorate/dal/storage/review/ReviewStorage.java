package ru.yandex.practicum.filmorate.dal.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Review create(Review review);

    Optional<Review> update(Review review);

    void delete(Long id);

    Optional<Review> findById(Long id);

    Collection<Review> findAll(Long filmId, Integer count);

    void setLike(Long reviewId, Long userId, boolean isPositive);

    void deleteLike(Long reviewId, Long userId, boolean isPositive);

}