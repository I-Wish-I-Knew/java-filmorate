package ru.yandex.practicum.filmorate.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storages.UserDbStorage;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserDbStorage storage;

    @Autowired
    public UserServiceImpl(UserDbStorage storage) {
        this.storage = storage;
    }

    @Override
    public User add(User user) {
        setName(user);
        storage.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        setName(user);
        checkExist(user.getId());
        storage.update(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = storage.getAll();
        log.debug(String.format("Общее количество пользователей в хранилище: %d", allUsers.size()));
        return allUsers;
    }

    @Override
    public User get(Integer id) {
        checkExist(id);
        return storage.get(id);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        checkExist(userId);
        List<User> friends = storage.getFriendsForUser(userId);
        log.debug(String.format("Общее количество друзей у пользователя %d: %d", userId,
                friends.size()));
        return friends;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        checkExist(userId);
        checkExist(friendId);
        storage.addFriend(userId, friendId);
        log.debug(String.format("Пользователи %d и %d стали друзьями", userId, friendId));
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        checkExist(userId);
        checkExist(friendId);
        storage.deleteFriend(userId, friendId);
        log.debug(String.format("Пользователи %d и %d больше не друзья", userId, friendId));
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer user2Id) {
        checkExist(userId);
        checkExist(user2Id);
        List<User> commonFriends = storage.getCommonFriends(userId, user2Id);
        log.debug(String.format("Общее количество общих друзей у пользователя %d и пользователя %d: %d", userId, user2Id,
                commonFriends.size()));
        return commonFriends;
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    private void checkExist(Integer id) {
        if (!storage.isExists(id)) {
            throw new NotFoundException(String.format("Пользователя с %d нет в списке", id));
        }
    }
}
