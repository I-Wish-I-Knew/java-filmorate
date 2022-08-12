package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.models.AppData;

import java.util.List;

public interface DataService<T extends AppData> {
    List<T> getAll();

    T get(Integer id);
}
