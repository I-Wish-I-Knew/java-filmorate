package ru.yandex.practicum.filmorate.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Mpa extends AppData {
    private final String name;

    public Mpa(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
