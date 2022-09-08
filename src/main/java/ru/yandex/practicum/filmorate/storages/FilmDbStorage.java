package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmDbStorage extends DataStorage<Film> {

    Film save(Film film);

    Film update(Film film);

    void saveLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getMostPopular(int count);

    void deleteFilmById(Integer filmId);
}
