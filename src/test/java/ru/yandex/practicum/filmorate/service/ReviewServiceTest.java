package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dal.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;

    @BeforeEach
    void setUp() {
        review = Review.builder()
                .id(1L)
                .filmId(1L)
                .userId(1L)
                .content("Test review")
                .isPositive(true)
                .useful(0)
                .build();
    }

    @Test
    void createReview() {
        when(reviewRepository.create(review)).thenReturn(review);

        Review createdReview = reviewService.createReview(review);

        assertEquals(review, createdReview);
        verify(reviewRepository, times(1)).create(review);
    }

    @Test
    void updateReview() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewRepository.update(review)).thenReturn(review);

        Review updatedReview = reviewService.updateReview(review);

        assertEquals(review, updatedReview);
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, times(1)).update(review);
    }

    @Test
    void updateReviewNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.updateReview(review));
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, never()).update(review);
    }

    @Test
    void deleteReview() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        reviewService.deleteReview(review.getId());

        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, times(1)).delete(review.getId());
    }

    @Test
    void deleteReviewNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.deleteReview(review.getId()));
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, never()).delete(review.getId());
    }

    @Test
    void getReviewById() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        Review foundReview = reviewService.getReviewById(review.getId());

        assertEquals(review, foundReview);
        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @Test
    void getReviewByIdNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.getReviewById(review.getId()));
        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @Test
    void getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        when(reviewRepository.findAll(review.getFilmId(), 10)).thenReturn(reviews);

        Collection<Review> foundReviews = reviewService.getAllReviews(review.getFilmId(), 10);

        assertEquals(reviews, foundReviews);
        verify(reviewRepository, times(1)).findAll(review.getFilmId(), 10);
    }

    @Test
    void addLike() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        reviewService.addLike(review.getId(), 1L);

        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, times(1)).setLike(review.getId(), 1L, true);
    }

    @Test
    void addLikeNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.addLike(review.getId(), 1L));
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, never()).setLike(review.getId(), 1L, true);
    }

    @Test
    void addDislike() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        reviewService.addDislike(review.getId(), 1L);

        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, times(1)).setLike(review.getId(), 1L, false);
    }

    @Test
    void addDislikeNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.addDislike(review.getId(), 1L));
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, never()).setLike(review.getId(), 1L, false);
    }

    @Test
    void deleteLike() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        reviewService.deleteLike(review.getId(), 1L);

        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, times(1)).deleteLike(review.getId(), 1L, true);
    }

    @Test
    void deleteLikeNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.deleteLike(review.getId(), 1L));
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, never()).deleteLike(review.getId(), 1L, true);
    }

    @Test
    void deleteDislike() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        reviewService.deleteDislike(review.getId(), 1L);

        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, times(1)).deleteLike(review.getId(), 1L, false);
    }

    @Test
    void deleteDislikeNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.deleteDislike(review.getId(), 1L));
        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, never()).deleteLike(review.getId(), 1L, false);
    }
}