package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.AppData;

import java.util.List;

public interface DataService<T extends AppData> {
    List<T> getAll();

    T get(Integer id);
}
