package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorStorage storage;

    @Autowired
    public DirectorServiceImpl(DirectorStorage storage) {
        this.storage = storage;
    }

    @Override
    public Director add(Director director) {
        return storage.save(director);
    }

    @Override
    public Director update(Director director) {
        checkDirectorExists(director.getId());
        return storage.update(director);
    }

    @Override
    public List<Director> getAll() {
        return storage.getAll();
    }

    @Override
    public Director get(Long id) {
        checkDirectorExists(id);
        return storage.get(id);
    }

    @Override
    public void delete(Long id) {
        checkDirectorExists(id);
        storage.delete(id);
    }

    private void checkDirectorExists(Long id) {
        if (!storage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Director with id = %d not found", id));
        }
    }
}
