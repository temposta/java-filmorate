package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

/**
 * Контроллер для обработки запросов, связанных с отзывами.
 */
@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Создает новый отзыв.
     *
     * @param review Отзыв для создания.
     * @return Созданный отзыв.
     */
    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        log.info("Создание отзыва: {}", review);
        return reviewService.createReview(review);
    }

    /**
     * Обновляет существующий отзыв.
     *
     * @param review Отзыв для обновления.
     * @return Обновленный отзыв.
     */
    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Обновление отзыва: {}", review);
        return reviewService.updateReview(review);
    }

    /**
     * Удаляет отзыв по его ID.
     *
     * @param id ID отзыва для удаления.
     */
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        log.info("Удаление отзыва с id: {}", id);
        reviewService.deleteReview(id);
    }

    /**
     * Получает отзыв по его ID.
     *
     * @param id ID отзыва для получения.
     * @return Отзыв с указанным ID.
     */
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        log.info("Получение отзыва по id: {}", id);
        return reviewService.getReviewById(id);
    }

    /**
     * Получает все отзывы для определенного фильма.
     *
     * @param filmId ID фильма.
     * @param count  Максимальное количество отзывов для получения.
     * @return Коллекция отзывов для указанного фильма.
     */
    @GetMapping
    public Collection<Review> getAllReviews(
            @RequestParam(required = false) Long filmId,
            @RequestParam(defaultValue = "10") Integer count) {
        log.info("Получение отзывов для filmId: {}, count: {}", filmId, count);
        return reviewService.getAllReviews(filmId, count);
    }

    /**
     * Добавляет лайк к отзыву от определенного пользователя.
     *
     * @param id     ID отзыва.
     * @param userId ID пользователя, который ставит лайк.
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление лайка к отзыву {} от пользователя {}", id, userId);
        reviewService.addLike(id, userId);
    }

    /**
     * Добавляет дизлайк к отзыву от определенного пользователя.
     *
     * @param id     ID отзыва.
     * @param userId ID пользователя, который ставит дизлайк.
     */
    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление дизлайка к отзыву {} от пользователя {}", id, userId);
        reviewService.addDislike(id, userId);
    }

    /**
     * Удаляет лайк из отзыва определенным пользователем.
     *
     * @param id     ID отзыва.
     * @param userId ID пользователя, который удаляет лайк.
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление лайка из отзыва {} пользователем {}", id, userId);
        reviewService.deleteLike(id, userId);
    }

    /**
     * Удаляет дизлайк из отзыва определенным пользователем.
     *
     * @param id     ID отзыва.
     * @param userId ID пользователя, который удаляет дизлайк.
     */
    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление дизлайка из отзыва {} пользователем {}", id, userId);
        reviewService.deleteDislike(id, userId);
    }
}

