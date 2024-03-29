package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    Mpa get(Integer id);

    List<Mpa> getAll();

    boolean containsInStorage(Integer id);
}
