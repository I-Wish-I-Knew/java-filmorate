package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.WrongDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSortBy;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final UserStorage userStorage;
    private final FilmStorage storage;
    private final DirectorStorage directorStorage;

    @Autowired
    public FilmServiceImpl(UserStorage userStorage,
                           FilmStorage storage,
                           DirectorStorage directorStorage) {
        this.userStorage = userStorage;
        this.storage = storage;
        this.directorStorage = directorStorage;
    }

    @Override
    public Film add(Film film) {
        validateDate(film.getReleaseDate());
        storage.save(film);
        return film;
    }

    public Film update(Film film) {
        checkExistFilm(film.getId());
        storage.update(film);
        return film;
    }

    public List<Film> getAll() {
        List<Film> films = storage.getAll();
        films.forEach(this::loadFilmFields);
        return films;
    }

    public Film get(Long id) {
        checkExistFilm(id);
        Film film = storage.get(id);
        loadFilmFields(film);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        checkExistUser(userId);
        checkExistFilm(filmId);
        storage.saveLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        checkExistUser(userId);
        checkExistFilm(filmId);
        storage.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopular(Integer count) {
        List<Film> films = storage.getMostPopular(count);
        films.forEach(this::loadFilmFields);
        return films;
    }

    public void delete(Long id) {
        checkExistFilm(id);
        storage.delete(id);
    }

    @Override
    public List<Film> getSortedDirectorFilms(Long directorId, FilmSortBy sortBy) {
        checkExistDirector(directorId);
        List<Film> films;
        if (sortBy.equals(FilmSortBy.YEAR)) {
            films = storage.getSortedDirectorsFilmsByYear(directorId);
        } else {
            films = storage.getSortedDirectorsFilmsByLikes(directorId);
        }
        films.forEach(this::loadFilmFields);
        return films;
    }

    private void checkExistFilm(Long id) {
        if (!storage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Film %d was not found", id));
        }
    }

    private void checkExistUser(Long id) {
        if (!userStorage.containsInStorage(id)) {
            throw new NotFoundException(String.format("User %d was not found", id));
        }
    }

    private void checkExistDirector(Long id) {
        if (!directorStorage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Director %d was not found", id));
        }
    }

    private void validateDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(LocalDate.parse("1895-12-28", DateTimeFormatter.ISO_DATE))) {
            RuntimeException e = new WrongDateException("Release date must be after 28.12.1895");
            log.error(e.getMessage());
            throw e;
        }
    }

    private void loadFilmFields(Film film) {
        film.getGenres().addAll(storage.loadFilmGenre(film));
        film.getLikes().addAll(storage.loadFilmLikes(film));
        film.getDirectors().addAll(storage.loadFilmDirectors(film));
    }
}
