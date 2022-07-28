package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.models.AppData;

import java.util.List;

public interface Service<T extends AppData> {
    T add(T t);

    T update(T t);

    List<T> getAll();

    T get(Integer id);
}
