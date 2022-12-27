package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AppData;

import java.util.List;

public interface DataStorage<T extends AppData> {
    T get(int id);

    List<T> getAll();

    boolean containsInStorage(int id);
}
