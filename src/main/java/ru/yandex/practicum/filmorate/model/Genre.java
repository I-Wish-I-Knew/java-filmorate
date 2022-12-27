package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Genre extends AppData {
    private String name;

    public Genre(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
