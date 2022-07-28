package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.AppData;
import ru.yandex.practicum.filmorate.storages.DataStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class ServiceImpl<T extends AppData> implements Service<T> {
    protected DataStorage<T> storage;

    public ServiceImpl(DataStorage<T> storage) {
        this.storage = storage;
    }

    public T add(T t) {
        validate(t);
        storage.add(t);
        log.debug("Был добавлен объект: " + t);
        return t;
    }

    public T update(T t) {
        checkExist(t.getId());
        storage.update(t);
        log.debug("Был обновлён объект: " + t);
        return t;
    }

    public List<T> getAll() {
        log.debug(String.format("Общее количество объектов в хранилище: %d", storage.getAll().size()));
        return new ArrayList<>(storage.getAll().values());
    }

    public T get(Integer id) {
        checkExist(id);
        return storage.get(id);
    }

    protected void checkExist(Integer id) {
        if (!storage.getAll().containsKey(id)) {
            throw new NotFoundException(String.format("Объекта с %d нет в списке", id));
        }
    }

    protected abstract void validate(T t);
}
