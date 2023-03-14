package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSortBy;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping("{filmId}")
    public Film get(@PathVariable Long filmId) {

        Film film = service.get(filmId);
        log.info("Get film {}", film.getId());
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        List<Film> films = service.getAll();
        log.info("Get all {} films", films.size());
        return films;
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        Film savedFilm = service.add(film);
        log.info("Film {} was saved", film);
        return savedFilm;
    }

    @PutMapping
    public Film update(@RequestBody @Validated(Update.class) Film film) {
        Film updatedFilm = service.update(film);
        log.info("Film {} was updated", film);
        return updatedFilm;
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable Long filmId) {
        service.delete(filmId);
        log.info("Film {} was deleted", filmId);
    }

    @PutMapping(value = "{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        service.addLike(id, userId);
        log.info("Like from user {} was added to film {} likes", userId, id);
        return service.get(id);
    }

    @DeleteMapping(value = "{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        service.deleteLike(id, userId);
        log.info("Like from user {} was deleted from film {} likes", userId, id);
        return service.get(id);
    }

    @GetMapping(value = {"popular?count={count}", "popular"})
    public List<Film> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10")
                                          Integer count) {
        List<Film> films = service.getMostPopular(count);
        log.info("Get {} most popular films", count);
        return films;
    }

    @GetMapping(value = "/director/{directorId}")
    public List<Film> getSortedFilmsByDirector(@PathVariable Long directorId,
                                               @RequestParam("sortBy") FilmSortBy sortBy) {
        List<Film> films = service.getSortedDirectorFilms(directorId, sortBy);
        log.info("Get sorted films by director");
        return films;
    }
}
