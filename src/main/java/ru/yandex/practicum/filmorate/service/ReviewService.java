package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repository.EventRepository;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.dal.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

import java.util.Collection;
import java.util.Optional;

/**
 * Сервис для работы с отзывами.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final EventService eventService;

    /**
     * Создает новый отзыв.
     *
     * @param review Отзыв для создания.
     * @return Созданный отзыв.
     */
    public Review createReview(Review review) {
        validateReview(review);// Проверка валидности отзыва
        Review createdReview = reviewStorage.create(review);
        eventService.saveEvent(review.getUserId(), EventType.REVIEW, OperationType.ADD, review.getReviewId());
        log.error("GET REVIEW ID: {}", review.getReviewId());
        return createdReview;
    }

    private void validateReview(Review review) {
        Optional<User> user = userStorage.read(review.getUserId());
        if (user.isEmpty()) {
            log.error(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, review.getUserId()));
            throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, review.getUserId()));
        }

        Optional<Film> film = filmStorage.read(review.getFilmId());
        if (film.isEmpty()) {
            log.error(ExceptionMessages.FILM_NOT_FOUNT_ERROR, review.getFilmId());
            throw new NotFoundException(String.format(ExceptionMessages.FILM_NOT_FOUNT_ERROR, review.getFilmId()));
        }
    }

    /**
     * Обновляет существующий отзыв.
     *
     * @param review Отзыв для обновления.
     * @return Обновленный отзыв.
     */
    public Review updateReview(Review review) {
        getReviewById(review.getReviewId()); // Проверка существования отзыва
        validateReview(review); // Проверка валидности отзыва
        eventService.saveEvent(review.getUserId(), EventType.REVIEW, OperationType.UPDATE, review.getReviewId());
        return reviewStorage.update(review).orElseThrow();
    }

    /**
     * Удаляет отзыв по его ID.
     *
     * @param id ID отзыва для удаления.
     */
    public void deleteReview(Long id) {
        Review reviewById = getReviewById(id);// Проверка существования отзыва
        eventService.saveEvent(reviewById.getUserId(), EventType.REVIEW, OperationType.REMOVE, id);
        reviewStorage.delete(id);
    }

    /**
     * Получает отзыв по его ID.
     *
     * @param id ID отзыва для получения.
     * @return Отзыв с указанным ID.
     * @throws NotFoundException если отзыв с указанным ID не найден.
     */
    public Review getReviewById(Long id) {
        return reviewStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.REVIEW_NOT_FOUND_ERROR, id)));
    }

    /**
     * Получает все отзывы для определенного фильма.
     *
     * @param filmId ID фильма.
     * @param count  Максимальное количество отзывов для получения.
     * @return Коллекция отзывов для указанного фильма.
     */
    public Collection<Review> getAllReviews(Long filmId, Integer count) {
        return reviewStorage.findAll(filmId, count);
    }

    /**
     * Добавляет лайк к отзыву от определенного пользователя.
     *
     * @param reviewId ID отзыва.
     * @param userId   ID пользователя, который ставит лайк.
     */
    public void addLike(Long reviewId, Long userId) {
        getReviewById(reviewId); // Проверка существования отзыва
        reviewStorage.setLike(reviewId, userId, true);
        eventService.saveEvent(userId, EventType.LIKE, OperationType.ADD, reviewId);
    }

    /**
     * Добавляет дизлайк к отзыву от определенного пользователя.
     *
     * @param reviewId ID отзыва.
     * @param userId   ID пользователя, который ставит дизлайк.
     */
    public void addDislike(Long reviewId, Long userId) {
        getReviewById(reviewId); // Проверка существования отзыва
        reviewStorage.setLike(reviewId, userId, false);
        eventService.saveEvent(userId, EventType.LIKE, OperationType.UPDATE, reviewId);
    }

    /**
     * Удаляет лайк из отзыва определенным пользователем.
     *
     * @param reviewId ID отзыва.
     * @param userId   ID пользователя, который удаляет лайк.
     */
    public void deleteLike(Long reviewId, Long userId) {
        eventService.saveEvent(userId, EventType.LIKE, OperationType.REMOVE, reviewId);
        reviewStorage.deleteLike(reviewId, userId);
    }

    /**
     * Удаляет дизлайк из отзыва определенным пользователем.
     *
     * @param reviewId ID отзыва.
     * @param userId   ID пользователя, который удаляет дизлайк.
     */
    public void deleteDislike(Long reviewId, Long userId) {
        reviewStorage.deleteLike(reviewId, userId);
        eventService.saveEvent(userId, EventType.LIKE, OperationType.REMOVE, reviewId);
    }
}

