package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storages.GenreDbStorage;

import java.util.List;

@Service
public class GenreService implements DataService<Genre> {
    private final GenreDbStorage storage;

    @Autowired
    public GenreService(GenreDbStorage storage) {
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
        if (storage.getAll().stream().noneMatch(genre -> genre.getId() == id)) {
            throw new NotFoundException(String.format("Жанра с %d нет в списке", id));
        }
    }
}
