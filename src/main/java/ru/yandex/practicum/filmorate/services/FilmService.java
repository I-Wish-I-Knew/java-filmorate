package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmService extends Service<Film> {
    Film addLike(Integer filmId, Integer userId);

    Film deleteLike(Integer filmId, Integer userId);

    List<Film> getMostPopular(Integer count);
}
