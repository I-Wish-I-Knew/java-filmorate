package ru.yandex.practicum.filmorate.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Film extends AppData {
    @NotBlank(message = "Укажите название фильма")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;
    @DecimalMin(value = "0", message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    private final Set<Integer> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
    @NonNull
    private Mpa mpa;
    private Integer rate = 0;

    public Film(Integer id, String name, String description, LocalDate releaseDate,
                Integer duration, Mpa mpa, Integer rate) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
    }
}
