package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

@Repository
public class ReviewRepository extends BaseRepository<Review> {

    private static final String INSERT_QUERY = "INSERT INTO reviews (content, is_positive, user_id, film_id) " +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE reviews SET content = ?, is_positive = ? WHERE review_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM reviews WHERE review_id = ?";
    private static final String FIND_ALL_WITH_LIMIT_QUERY = "SELECT * FROM reviews ORDER BY useful DESC LIMIT ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM reviews WHERE review_id = ?";
    private static final String FIND_BY_FILM_ID_WITH_LIMIT_QUERY = "SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?";

    private static final String SET_LIKE_QUERY = "MERGE INTO review_likes(review_id, user_id, is_positive) KEY(review_id, user_id) " +
            "SELECT ?, ?, ? FROM DUAL";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";
    private static final String UPDATE_USEFUL_QUERY = "UPDATE reviews r SET useful = " +
            "(SELECT COALESCE(COUNT(CASE WHEN rl.is_positive THEN 1 END) - COUNT(CASE WHEN NOT rl.is_positive THEN 1 END), 0) " +
            "FROM review_likes rl WHERE rl.review_id = r.review_id) " +
            "WHERE r.review_id = ?";

    public ReviewRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Review create(Review review) {

        Long id = insertWithGeneratedId(INSERT_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId()
        );
        review.setReviewId(id);
        review.setUseful(0);

        return review;
    }

    public Optional<Review> update(Review review) {
        update(UPDATE_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        updateUsefulRating(review.getReviewId());

        return findOne(FIND_BY_ID_QUERY, review.getReviewId());
    }

    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

    public Optional<Review> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Collection<Review> findAll(Long filmId, Integer count) {
        if (filmId != null) {
            return findMany(FIND_BY_FILM_ID_WITH_LIMIT_QUERY, filmId, count);
        } else {
            return findMany(FIND_ALL_WITH_LIMIT_QUERY, count);
        }
    }

    public void setLike(Long reviewId, Long userId, boolean isPositive) {
        update(SET_LIKE_QUERY, reviewId, userId, isPositive);
        updateUsefulRating(reviewId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        update(DELETE_LIKE_QUERY, reviewId, userId);
        updateUsefulRating(reviewId);
    }

    private void updateUsefulRating(Long reviewId) {
        update(UPDATE_USEFUL_QUERY, reviewId);
    }
}