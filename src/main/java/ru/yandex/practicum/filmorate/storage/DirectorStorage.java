package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

public interface DirectorStorage extends DataStorage<Director> {
    Director save(Director director);

    Director update(Director director);

    void delete(Integer directorId);
}
