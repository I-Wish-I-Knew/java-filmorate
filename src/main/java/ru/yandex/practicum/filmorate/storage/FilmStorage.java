package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmStorage {

    Film get(Long id);

    List<Film> getAll();

    boolean containsInStorage(Long id);

    Film save(Film film);

    Film update(Film film);

    void delete(Long id);

    void saveLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getMostPopular(int count);

    List<Film> getSortedDirectorsFilmsByLikes(Long directorId);

    List<Film> getSortedDirectorsFilmsByYear(Long directorId);

    List<Long> loadFilmLikes(Film film);

    List<Genre> loadFilmGenre(Film film);

    List<Director> loadFilmDirectors(Film film);
}
