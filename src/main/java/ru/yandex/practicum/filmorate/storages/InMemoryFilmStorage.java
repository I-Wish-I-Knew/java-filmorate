package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements DataStorage<Film> {
    private final Map<Integer, Film> films;
    private int id = 0;

    public InMemoryFilmStorage() {
        films = new LinkedHashMap<>();
    }

    public Map<Integer, Film> getAll() {
        return films;
    }

    public Film add(Film film) {
        film.setId(getId());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film get(Integer filmId) {
        return films.get(filmId);
    }

    private int getId() {
        return ++id;
    }

}