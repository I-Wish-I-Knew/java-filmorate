package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review save(Review review);

    Review update(Review review);

    List<Review> get(Integer count);

    List<Review> getAllByFilmId(Integer count, Long filmId);

    Review getById(Long id);

    void delete(Long id);

    boolean containsInStorage(Long id);

}

