package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.DataStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private DataStorage<Film> storage;
    @Autowired
    private DataStorage<User> userStorage;

    public Film add(Film film) {
        storage.add(film);
        return film;
    }

    public Film update(Film film) {
        checkExistFilm(film.getId());
        storage.update(film);
        return film;
    }

    public List<Film> getAll() {
        return new ArrayList<>(storage.getAll().values());
    }

    public Film get(Integer id) {
        checkExistFilm(id);
        return storage.get(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkExistUser(userId);
        checkExistFilm(filmId);
        storage.getAll().get(filmId).getLikes().add(userId);
        return storage.getAll().get(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkExistUser(userId);
        checkExistFilm(filmId);
        storage.getAll().get(filmId).getLikes().remove(userId);
        return storage.getAll().get(filmId);
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
}
