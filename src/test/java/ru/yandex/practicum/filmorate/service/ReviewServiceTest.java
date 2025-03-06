package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.dal.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewStorage reviewStorage;

    @Mock
    private FilmStorage filmStorage;

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private User testUser;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(2000, 6, 15))
                .build();

        testFilm = Film.builder()
                .id(1L)
                .name("Test Film")
                .description("Test Description")
                .mpa(new Mpa(1L, "G"))
                .genres(Set.of(new Genre(1L, "Комедия")))
                .build();

        review = Review.builder()
                .reviewId(1L)
                .filmId(testFilm.getId())
                .userId(testUser.getId())
                .content("Test review")
                .isPositive(true)
                .useful(0)
                .build();
    }

    @Test
    void updateReviewNotFound() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.updateReview(review));
        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, never()).update(review);
    }

    @Test
    void deleteReview() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.of(review));

        reviewService.deleteReview(review.getReviewId());

        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, times(1)).delete(review.getReviewId());
    }

    @Test
    void deleteReviewNotFound() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.deleteReview(review.getReviewId()));
        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, never()).delete(review.getReviewId());
    }

    @Test
    void getReviewById() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.of(review));

        Review foundReview = reviewService.getReviewById(review.getReviewId());

        assertEquals(review, foundReview);
        verify(reviewStorage, times(1)).findById(review.getReviewId());
    }

    @Test
    void getReviewByIdNotFound() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.getReviewById(review.getReviewId()));
        verify(reviewStorage, times(1)).findById(review.getReviewId());
    }

    @Test
    void getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        when(reviewStorage.findAll(review.getFilmId(), 10)).thenReturn(reviews);

        Collection<Review> foundReviews = reviewService.getAllReviews(review.getFilmId(), 10);

        assertEquals(reviews, foundReviews);
        verify(reviewStorage, times(1)).findAll(review.getFilmId(), 10);
    }

    @Test
    void addLike() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.of(review));

        reviewService.addLike(review.getReviewId(), 1L);

        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, times(1)).setLike(review.getReviewId(), 1L, true);
    }

    @Test
    void addLikeNotFound() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.addLike(review.getReviewId(), 1L));
        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, never()).setLike(review.getReviewId(), 1L, true);
    }

    @Test
    void addDislike() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.of(review));

        reviewService.addDislike(review.getReviewId(), 1L);

        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, times(1)).setLike(review.getReviewId(), 1L, false);
    }

    @Test
    void addDislikeNotFound() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.addDislike(review.getReviewId(), 1L));
        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, never()).setLike(review.getReviewId(), 1L, false);
    }

    @Test
    void deleteLike() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.of(review));

        reviewService.deleteLike(review.getReviewId(), 1L);

        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, times(1)).deleteLike(review.getReviewId(), 1L, true);
    }

    @Test
    void deleteLikeNotFound() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.deleteLike(review.getReviewId(), 1L));
        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, never()).deleteLike(review.getReviewId(), 1L, true);
    }

    @Test
    void deleteDislike() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.of(review));

        reviewService.deleteDislike(review.getReviewId(), 1L);

        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, times(1)).deleteLike(review.getReviewId(), 1L, false);
    }

    @Test
    void deleteDislikeNotFound() {
        when(reviewStorage.findById(review.getReviewId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.deleteDislike(review.getReviewId(), 1L));
        verify(reviewStorage, times(1)).findById(review.getReviewId());
        verify(reviewStorage, never()).deleteLike(review.getReviewId(), 1L, false);
    }
}