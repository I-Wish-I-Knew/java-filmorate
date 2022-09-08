package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Mpa extends AppData {
    private String name;

    public Mpa(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
