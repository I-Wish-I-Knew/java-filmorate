package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.impl.MpaStorageImpl;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {

    private final MpaStorageImpl storage;

    public MpaServiceImpl(MpaStorageImpl storage) {
        this.storage = storage;
    }

    @Override
    public List<Mpa> getAll() {
        return storage.getAll();
    }

    @Override
    public Mpa get(Integer id) {
        checkExist(id);
        return storage.get(id);
    }

    private void checkExist(Integer id) {
        if (!storage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Mpa %d was not found", id));
        }
    }
}
