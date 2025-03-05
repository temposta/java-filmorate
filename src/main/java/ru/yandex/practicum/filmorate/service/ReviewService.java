package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

/**
 * Сервис для работы с отзывами.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Создает новый отзыв.
     *
     * @param review Отзыв для создания.
     * @return Созданный отзыв.
     */
    public Review createReview(Review review) {
        return reviewRepository.create(review);
    }

    /**
     * Обновляет существующий отзыв.
     *
     * @param review Отзыв для обновления.
     * @return Обновленный отзыв.
     */
    public Review updateReview(Review review) {
        getReviewById(review.getId()); // Проверка существования отзыва
        return reviewRepository.update(review);
    }

    /**
     * Удаляет отзыв по его ID.
     *
     * @param id ID отзыва для удаления.
     */
    public void deleteReview(Long id) {
        getReviewById(id); // Проверка существования отзыва
        reviewRepository.delete(id);
    }

    /**
     * Получает отзыв по его ID.
     *
     * @param id ID отзыва для получения.
     * @return Отзыв с указанным ID.
     * @throws NotFoundException если отзыв с указанным ID не найден.
     */
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review with id " + id + " not found"));
    }

    /**
     * Получает все отзывы для определенного фильма.
     *
     * @param filmId ID фильма.
     * @param count  Максимальное количество отзывов для получения.
     * @return Коллекция отзывов для указанного фильма.
     */
    public Collection<Review> getAllReviews(Long filmId, Integer count) {
        return reviewRepository.findAll(filmId, count);
    }

    /**
     * Добавляет лайк к отзыву от определенного пользователя.
     *
     * @param reviewId ID отзыва.
     * @param userId   ID пользователя, который ставит лайк.
     */
    public void addLike(Long reviewId, Long userId) {
        getReviewById(reviewId); // Проверка существования отзыва
        reviewRepository.setLike(reviewId, userId, true);
    }

    /**
     * Добавляет дизлайк к отзыву от определенного пользователя.
     *
     * @param reviewId ID отзыва.
     * @param userId   ID пользователя, который ставит дизлайк.
     */
    public void addDislike(Long reviewId, Long userId) {
        getReviewById(reviewId); // Проверка существования отзыва
        reviewRepository.setLike(reviewId, userId, false);
    }

    /**
     * Удаляет лайк из отзыва определенным пользователем.
     *
     * @param reviewId ID отзыва.
     * @param userId   ID пользователя, который удаляет лайк.
     */
    public void deleteLike(Long reviewId, Long userId) {
        getReviewById(reviewId); // Проверка существования отзыва
        reviewRepository.deleteLike(reviewId, userId, true);
    }

    /**
     * Удаляет дизлайк из отзыва определенным пользователем.
     *
     * @param reviewId ID отзыва.
     * @param userId   ID пользователя, который удаляет дизлайк.
     */
    public void deleteDislike(Long reviewId, Long userId) {
        getReviewById(reviewId); // Проверка существования отзыва
        reviewRepository.deleteLike(reviewId, userId, false);
    }
}

