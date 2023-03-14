package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Reaction;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReactionStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage storage;
    private final UserStorage userStorage;
    private final ReactionStorage reactionStorage;
    private final FilmStorage filmStorage;

    public ReviewServiceImpl(ReviewStorage storage,
                             UserStorage userStorage,
                             ReactionStorage reactionStorage,
                             FilmStorage filmStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.reactionStorage = reactionStorage;
        this.filmStorage = filmStorage;
    }

    public Review save(Review review) {
        review.setUseful(0);
        isUserExists(review.getUserId());
        isFilmExists(review.getFilmId());
        return storage.save(review);
    }

    public Review update(Review review) {
        isUserExists(review.getUserId());
        isFilmExists(review.getFilmId());
        isReviewExists(review.getReviewId());
        return storage.update(review);
    }

    public List<Review> getAll(Integer count, Long filmId) {
        List<Review> reviews;
        if (filmId != null) {
            isFilmExists(filmId);
            reviews = storage.getAllByFilmId(count, filmId);
        } else {
            reviews = storage.get(count);
        }
        reviews.forEach(review ->
                addReactionForReview(review, reactionStorage.getReactions(review.getReviewId())));
        return reviews.stream()
                .sorted(Comparator.comparingInt(Review::getUseful).reversed())
                .collect(Collectors.toList());
    }

    public Review get(Long reviewId) {
        isReviewExists(reviewId);
        Review review = storage.getById(reviewId);
        List<Reaction> reactions = reactionStorage.getReactions(reviewId);
        addReactionForReview(review, reactions);
        return review;
    }

    public void deleteReview(Long reviewId) {
        isReviewExists(reviewId);
        storage.delete(reviewId);
    }

    public void saveLike(Long reviewId, Long userId) {
        isReviewExists(reviewId);
        isUserExists(userId);
        reactionStorage.saveLike(reviewId, userId);
    }

    public void saveDislike(Long reviewId, Long userId) {
        isReviewExists(reviewId);
        isUserExists(userId);
        reactionStorage.saveDislike(reviewId, userId);
    }

    public void deleteReaction(Long reviewId, Long userId) {
        isReactionExists(reviewId, userId);
        reactionStorage.deleteReaction(reviewId, userId);
    }

    private void isUserExists(Long userId) {
        if (!userStorage.containsInStorage(userId)) {
            throw new NotFoundException(String.format("User %d was not found", userId));
        }
    }

    private void isFilmExists(Long filmId) {
        if (!filmStorage.containsInStorage(filmId)) {
            throw new NotFoundException(String.format("Film %d was not found", filmId));
        }
    }

    private void isReviewExists(Long reviewId) {
        if (!storage.containsInStorage(reviewId)) {
            throw new NotFoundException(String.format("Review %d was not found", reviewId));
        }
    }

    private void isReactionExists(Long reviewId, Long userId) {
        if (!reactionStorage.containsReactionInStorage(reviewId, userId)) {
            throw new NotFoundException(String.format("Reaction on review %d by user %d was not found", reviewId, userId));
        }
    }

    private void addReactionForReview(Review review, List<Reaction> reactions) {
        reactions.forEach(reaction -> review.getUserReactions().add(reaction));
    }
}