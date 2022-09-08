package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.DataService;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;

import java.util.List;

@Service
public class MpaService implements DataService<Mpa> {

    private final MpaDbStorage storage;

    @Autowired
    public MpaService(MpaDbStorage storage) {
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
            throw new NotFoundException(String.format("Мпа с %d нет в списке", id));
        }
    }
}
