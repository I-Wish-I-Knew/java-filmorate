package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.WrongDateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.DataStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceImpl extends ServiceImpl<Film> implements FilmService {
    private final DataStorage<User> userStorage;

    @Autowired
    public FilmServiceImpl(DataStorage<Film> storage, DataStorage<User> userStorage) {
        super(storage);
        this.userStorage = userStorage;
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = storage.getAll().get(filmId);
        checkExistUser(userId);
        checkExistFilm(filmId);
        film.getLikes().add(userId);
        log.debug(String.format("Пользователь %d поставил like фильму %d", userId, filmId));
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = storage.getAll().get(filmId);
        checkExistUser(userId);
        checkExistFilm(filmId);
        film.getLikes().remove(userId);
        log.debug(String.format("Пользователю %d больше не нравится фильм %d", userId, filmId));
        return film;
    }

    public List<Film> getMostPopular(Integer count) {
        if (count == null) {
            count = 10;
        }
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
        return storage.getAll().values().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkExistFilm(Integer id) {
        if (!storage.getAll().containsKey(id)) {
            throw new NotFoundException(String.format("Фильма с %d нет в списке", id));
        }
    }

    private void checkExistUser(Integer id) {
        if (!userStorage.getAll().containsKey(id)) {
            throw new NotFoundException(String.format("Пользователя с %d нет в списке", id));
        }
    }

    protected void validate(Film film) {
        List<Film> films = getAll();
        if (films.stream()
                .anyMatch(f -> f.equals(film))) {
            RuntimeException e = new DataAlreadyExistException("Фильм уже был добавлен ранее");
            log.error(e.getMessage());
            throw e;
        }
        if (!validateDate(film.getReleaseDate())) {
            RuntimeException e = new WrongDateException("Дата выпуска не может быть ранее 28 декабря 1895");
            log.error(e.getMessage());
            throw e;
        }
    }

    private boolean validateDate(LocalDate releaseDate) {
        return !releaseDate.isBefore(LocalDate.parse("1895-12-28", DateTimeFormatter.ISO_DATE));
    }
}
