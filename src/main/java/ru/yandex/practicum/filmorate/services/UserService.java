package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserService {
    User add(User user);

    User update(User User);

    List<User> getAll();

    User get(Integer id);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getCommonFriends(Integer userId, Integer user2Id);

    List<User> getUserFriends(Integer userId);
}
