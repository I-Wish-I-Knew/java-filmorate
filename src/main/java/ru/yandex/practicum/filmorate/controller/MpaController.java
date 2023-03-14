package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @Autowired
    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping("{mpaId}")
    public Mpa get(@PathVariable int mpaId) {
        Mpa mpa = service.get(mpaId);
        log.info("Get mpa {}", mpaId);
        return mpa;
    }

    @GetMapping
    public List<Mpa> getAll() {
        List<Mpa> allMpa = service.getAll();
        log.info("Get all Mpa");
        return allMpa;
    }
}
