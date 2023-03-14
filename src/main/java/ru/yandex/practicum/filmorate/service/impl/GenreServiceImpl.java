package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.impl.GenreStorageImpl;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreStorageImpl storage;

    @Autowired
    public GenreServiceImpl(GenreStorageImpl storage) {
        this.storage = storage;
    }

    @Override
    public List<Genre> getAll() {
        return storage.getAll();
    }

    @Override
    public Genre get(Integer id) {
        checkExist(id);
        return storage.get(id);
    }

    private void checkExist(Integer id) {
        if (!storage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Genre %d was not found", id));
        }
    }
}
