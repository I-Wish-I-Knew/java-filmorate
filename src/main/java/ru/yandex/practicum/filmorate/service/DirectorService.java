package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

public interface DirectorService extends DataService<Director> {

    Director add(Director director);

    Director update(Director director);

    void delete(Integer id);
}
