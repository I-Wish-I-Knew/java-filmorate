package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/{id}")
    public Review get(@PathVariable Long id) {
        Review review = reviewService.get(id);
        log.info("Get review {}", review.getReviewId());
        return review;
    }

    @GetMapping
    public List<Review> getAll(@RequestParam(defaultValue = "10", required = false) Integer count,
                               @RequestParam(required = false) Long filmId) {
        List<Review> reviews = reviewService.getAll(count, filmId);
        log.info("Get all reviews, count = {}", count);
        return reviews;
    }

    @PostMapping
    public Review add(@RequestBody @Valid Review review) {
        Review savedReview = reviewService.save(review);
        log.info("Review {} was saved", savedReview);
        return savedReview;
    }

    @PutMapping
    public Review update(@RequestBody @Validated(Update.class) Review review) {
        Review updatedReview = reviewService.update(review);
        log.info("Review {} was updated", review);
        return updatedReview;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        log.info("Review {} was deleted", id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.saveLike(id, userId);
        log.info("Like for review {} from user {} was saved", id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.saveDislike(id, userId);
        log.info("Dislike for review {} from user {} was saved", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.deleteReaction(id, userId);
        log.info("Like for review {} from user {} was deleted", id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable Long id, @PathVariable Long userId) {
        reviewService.deleteReaction(id, userId);
        log.info("Dislike for review {} from user {} was deleted", id, userId);
    }
}


