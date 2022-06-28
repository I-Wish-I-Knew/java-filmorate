package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.WrongDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private boolean validateDate(LocalDate releaseDate) {
        return !releaseDate.isBefore(LocalDate.parse("1895-12-28", DateTimeFormatter.ISO_DATE));
    }

    @Override
    protected void validate(Film film) {
        if (data.values().stream()
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
}
