package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.WrongDateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.FilmDbStorage;
import ru.yandex.practicum.filmorate.storages.UserDbStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final UserDbStorage userStorage;
    private final FilmDbStorage storage;

    @Autowired
    public FilmServiceImpl(UserDbStorage userStorage, FilmDbStorage storage) {
        this.userStorage = userStorage;
        this.storage = storage;
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
        List<Film> allFilms = new ArrayList<>(storage.getAll().values());
        log.debug(String.format("Общее количество фильмов в хранилище: %d", allFilms.size()));
        return allFilms;
    }

    public Film get(Integer id) {
        checkExistFilm(id);
        return storage.get(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkExistUser(userId);
        checkExistFilm(filmId);
        log.debug(String.format("Пользователь %d поставил like фильму %d", userId, filmId));
        return storage.addLike(filmId, userId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkExistUser(userId);
        checkExistFilm(filmId);
        log.debug(String.format("Пользователю %d больше не нравится фильм %d", userId, filmId));
        return storage.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopular(Integer count) {
        return storage.getMostPopular(count);
    }

    private void checkExistFilm(Integer id) {
        if (!storage.isExists(id)) {
            throw new NotFoundException(String.format("Фильма с %d нет в списке", id));
        }
    }

    private void checkExistUser(Integer id) {
        if (!userStorage.isExists(id)) {
            throw new NotFoundException(String.format("Пользователя с %d нет в списке", id));
        }
    }

    private void validateDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(LocalDate.parse("1895-12-28", DateTimeFormatter.ISO_DATE))) {
            RuntimeException e = new WrongDateException("Дата выпуска не может быть ранее 28 декабря 1895");
            log.error(e.getMessage());
            throw e;
        }
    }
}
