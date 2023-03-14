package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSortBy;

import java.util.List;

public interface FilmService {

    List<Film> getAll();

    Film get(Long id);

    Film add(Film film);

    Film update(Film film);

    void delete(Long id);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getMostPopular(Integer count);

    List<Film> getSortedDirectorFilms(Long directorId, FilmSortBy sortBy);
}
