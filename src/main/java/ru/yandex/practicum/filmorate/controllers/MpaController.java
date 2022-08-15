package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.services.DataService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final DataService<Mpa> service;

    @Autowired
    public MpaController(DataService<Mpa> service) {
        this.service = service;
    }

    @GetMapping("{mpaId}")
    public Mpa getFilm(@PathVariable int mpaId) {
        return service.get(mpaId);
    }

    @GetMapping
    public List<Mpa> getAll() {
        return service.getAll();
    }
}
