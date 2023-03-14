package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "reviewId")
public class Review {
    @NotNull(groups = {Update.class})
    private Long reviewId;
    @NotBlank
    private String content;
    private Integer useful;
    @JsonProperty(required = true)
    @NotNull
    private Boolean isPositive;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    private Set<Reaction> userReactions = new HashSet<>();
}
