package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    List<Director> getAll();

    Director get(Long id);

    Director add(Director director);

    Director update(Director director);

    void delete(Long id);
}
