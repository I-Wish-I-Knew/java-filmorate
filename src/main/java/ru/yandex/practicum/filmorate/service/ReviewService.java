package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {
    Review save(Review review);

    Review update(Review review);

    List<Review> getAll(Integer count, Long filmId);

    Review get(Long reviewId);

    void deleteReview(Long reviewId);

    void saveLike(Long reviewId, Long userId);

    void saveDislike(Long reviewId, Long userId);

    void deleteReaction(Long reviewId, Long userId);
}
