package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.AppData;

import java.util.List;

public interface DataStorage<T extends AppData> {
    T get(int id);

    List<T> getAll();

    boolean isExists(int id);
}
