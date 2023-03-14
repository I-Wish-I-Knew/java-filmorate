package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;
    @DecimalMin(value = "0")
    private Integer duration;
    private final Set<Long> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
    private final Set<Director> directors = new HashSet<>();
    @NonNull
    private Mpa mpa;
    private Integer rate = 0;

}
