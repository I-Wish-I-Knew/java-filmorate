package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserService extends Service<User> {
    User addFriend(Integer userId, Integer friendId);

    User deleteFriend(Integer userId, Integer friendId);

    List<User> getCommonFriends(Integer userId, Integer user2Id);

    List<User> getUserFriends(Integer userId);
}
