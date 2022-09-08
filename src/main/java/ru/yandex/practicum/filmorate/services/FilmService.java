package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmService {
    Film add(Film film);
    Film update(Film film);

    List<Film> getAll();

    Film get(Integer id);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    List<Film> getMostPopular(Integer count);

    void deleteFilmById(Integer filmId);
}
