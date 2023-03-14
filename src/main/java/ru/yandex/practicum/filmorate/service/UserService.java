package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User get(Long id);

    User add(User user);

    User update(User user);

    void delete(Long id);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getCommonFriends(Long userId, Long anotherUserId);

    List<User> getUserFriends(Long id);
}
