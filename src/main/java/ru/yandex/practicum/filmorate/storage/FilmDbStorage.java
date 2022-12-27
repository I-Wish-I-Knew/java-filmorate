package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmDbStorage extends DataStorage<Film> {

    Film save(Film film);

    Film update(Film film);

    void delete(Integer filmId);

    void saveLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getMostPopular(int count);

    List<Film> getSortedDirectorsFilmsByLikes(int directorId);

    List<Film> getSortedDirectorsFilmsByYear(int directorId);

    List<Integer> loadFilmLikes(Film film);

    List<Genre> loadFilmGenre(Film film);

    List<Director> loadFilmDirectors(Film film);
}
