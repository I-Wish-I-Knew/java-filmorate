package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
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
    private final Set<Integer> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
    private final MpaRating mpaRating;
    private int rate = 0;
}
