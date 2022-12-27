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
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final UserDbStorage userStorage;
    private final FilmDbStorage storage;
    private final DirectorStorage directorStorage;

    @Autowired
    public FilmServiceImpl(UserDbStorage userStorage, FilmDbStorage storage, DirectorStorage directorStorage) {
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

    public Film get(Integer id) {
        checkExistFilm(id);
        Film film = storage.get(id);
        loadFilmFields(film);
        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        checkExistUser(userId);
        checkExistFilm(filmId);
        storage.saveLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        checkExistUser(userId);
        checkExistFilm(filmId);
        storage.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopular(Integer count) {
        List<Film> films = storage.getMostPopular(count);
        films.forEach(this::loadFilmFields);
        return films;
    }

    public void delete(Integer id) {
        checkExistFilm(id);
        storage.delete(id);
    }

    @Override
    public List<Film> getSortedDirectorsFilms(int directorId, FilmSortBy sortBy) {
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

    private void checkExistFilm(Integer id) {
        if (!storage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Фильма с %d нет в списке", id));
        }
    }

    private void checkExistUser(Integer id) {
        if (!userStorage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Пользователя с %d нет в списке", id));
        }
    }

    private void checkExistDirector(Integer id) {
        if (!directorStorage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Директора с %d нет в списке", id));
        }
    }

    private void validateDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(LocalDate.parse("1895-12-28", DateTimeFormatter.ISO_DATE))) {
            RuntimeException e = new WrongDateException("Дата выпуска не может быть ранее 28 декабря 1895");
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
