package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.WrongDateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    FilmService service;

    @GetMapping("{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        return service.get(filmId);
    }

    @GetMapping
    public List<Film> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        validate(film);
        Film createdFilm = service.add(film);
        log.debug("Был добавлен фильм: " + createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        Film updatedFilm = service.update(film);
        log.debug("Был обновлён фильм: " + updatedFilm);
        return updatedFilm;
    }

    @PutMapping(value = "{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        service.addLike(id, userId);
        log.debug(String.format("Пользователь %d поставил like фильму %d", userId, id));
        return service.get(id);
    }

    @DeleteMapping(value = "{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        service.deleteLike(id, userId);
        log.debug(String.format("Пользователю %d больше не нравится фильм %d", userId, id));
        return service.get(id);
    }

    @GetMapping(value = {"popular?count={count}", "popular"})
    public List<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        return service.getMostPopular(count);
    }

    private void validate(Film film) {
        List<Film> films = service.getAll();
        if (films.stream()
                .anyMatch(f -> f.equals(film))) {
            RuntimeException e = new DataAlreadyExistException("Фильм уже был добавлен ранее");
            log.error(e.getMessage());
            throw e;
        }
        if (!validateDate(film.getReleaseDate())) {
            RuntimeException e = new WrongDateException("Дата выпуска не может быть ранее 28 декабря 1895");
            log.error(e.getMessage());
            throw e;
        }
    }

    private boolean validateDate(LocalDate releaseDate) {
        return !releaseDate.isBefore(LocalDate.parse("1895-12-28", DateTimeFormatter.ISO_DATE));
    }
}
