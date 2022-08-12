package ru.yandex.practicum.filmorate.models;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class AppData {
    protected Integer id;
}
