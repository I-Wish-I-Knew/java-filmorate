package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.DataService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final DataService<Genre> service;

    @Autowired
    public GenreController(DataService<Genre> service) {
        this.service = service;
    }

    @GetMapping("{genreId}")
    public Genre getFilm(@PathVariable int genreId) {
        return service.get(genreId);
    }

    @GetMapping
    public List<Genre> getAll() {
        return service.getAll();
    }
}
