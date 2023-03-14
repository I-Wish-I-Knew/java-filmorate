package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Reaction {
    private ReactionType reactionType;
    private Long userId;
}
