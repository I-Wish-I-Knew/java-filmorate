package ru.yandex.practicum.filmorate.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmSortBy;

@Component
public class FilmSortByEnumConverter implements Converter<String, FilmSortBy> {

    @Override
    public FilmSortBy convert(String value) {
        return FilmSortBy.valueOf(value.toUpperCase());
    }
}
