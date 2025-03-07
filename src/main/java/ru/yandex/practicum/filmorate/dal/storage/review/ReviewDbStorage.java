package ru.yandex.practicum.filmorate.dal.storage.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    final ReviewRepository reviewRepository;

    @Override
    public Review create(Review review) {
        return reviewRepository.create(review);
    }

    @Override
    public Optional<Review> update(Review review) {
        return reviewRepository.update(review);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.delete(id);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Collection<Review> findAll(Long filmId, Integer count) {
        return reviewRepository.findAll(filmId, count);
    }

    @Override
    public void setLike(Long reviewId, Long userId, boolean isPositive) {
        reviewRepository.setLike(reviewId, userId, isPositive);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        reviewRepository.deleteLike(reviewId, userId);
    }
}
