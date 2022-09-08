package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSortBy;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping("{filmId}")
    public Film get(@PathVariable int filmId) {
        return service.get(filmId);
    }

    @GetMapping
    public List<Film> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        return service.add(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return service.update(film);
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable Integer filmId) {
        service.delete(filmId);
    }

    @PutMapping(value = "{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        service.addLike(id, userId);
        return service.get(id);
    }

    @DeleteMapping(value = "{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        service.deleteLike(id, userId);
        return service.get(id);
    }

    @GetMapping(value = {"popular?count={count}", "popular"})
    public List<Film> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10")
                                          Integer count) {
        return service.getMostPopular(count);
    }

    @GetMapping(value = "/director/{directorId}")
    public List<Film> getSortedFilmsByDirector(@PathVariable Integer directorId,
                                               @RequestParam("sortBy") FilmSortBy sortBy) {
        return service.getSortedDirectorsFilms(directorId, sortBy);
    }
}
