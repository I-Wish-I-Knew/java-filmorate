package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.AppData;

import java.util.Map;

public interface DataStorage<T extends AppData> {
    T get(Integer id);

    Map<Integer, T> getAll();

    T add(T t);

    T update(T t);
}
