package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
public class Film extends AppData {
    @NotBlank(message = "Укажите название фильма")
    private final String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private final String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate releaseDate;
    @DecimalMin(value = "0", message = "Продолжительность фильма должна быть положительной")
    private final int duration;
}
