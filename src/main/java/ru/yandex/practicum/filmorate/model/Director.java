package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Director extends AppData {
    @NotBlank
    private String name;

    public Director(Integer id, String name) {
        super(id);
        this.name = name;
    }
}