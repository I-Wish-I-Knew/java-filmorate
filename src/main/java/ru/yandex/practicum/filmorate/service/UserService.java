package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService extends DataService<User> {
    User add(User user);

    User update(User user);

    void delete(Integer id);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getCommonFriends(Integer userId, Integer anotherUserId);

    List<User> getUserFriends(Integer id);
}
