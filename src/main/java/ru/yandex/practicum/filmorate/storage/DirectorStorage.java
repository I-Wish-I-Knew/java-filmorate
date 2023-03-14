package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {

    Director get(Long id);

    List<Director> getAll();

    boolean containsInStorage(Long id);

    Director save(Director director);

    Director update(Director director);

    void delete(Long directorId);
}
