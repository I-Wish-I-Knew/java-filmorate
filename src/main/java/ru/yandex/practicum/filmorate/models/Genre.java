package ru.yandex.practicum.filmorate.models;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
public class Genre extends AppData{
    private String name;

    public Genre(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
