package ru.yandex.practicum.filmorate.models;

import lombok.*;
@Getter
@Setter
public class Genre extends AppData{
    private final String name;

    public Genre(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
