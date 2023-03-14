package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Reaction;

import java.util.List;

public interface ReactionStorage {

    List<Reaction> getReactions(Long reviewId);

    void saveLike(Long reviewId, Long userId);

    void saveDislike(Long reviewId, Long userId);

    void deleteReaction(Long reviewId, Long userId);

    boolean containsReactionInStorage(Long reviewId, Long userId);

}
