package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSortBy;

import java.util.List;

public interface FilmService extends DataService<Film> {
    Film add(Film film);

    Film update(Film film);

    void delete(Integer id);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    List<Film> getMostPopular(Integer count);

    List<Film> getSortedDirectorsFilms(int directorId, FilmSortBy sortBy);
}
