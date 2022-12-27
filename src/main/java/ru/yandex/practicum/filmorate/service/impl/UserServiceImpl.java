package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

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

    public void delete(Integer id) {
        checkExist(id);
        storage.delete(id);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        checkExist(id);
        List<User> friends = storage.getFriendsByUser(id);
        log.debug(String.format("Общее количество друзей у пользователя %d: %d", id,
                friends.size()));
        return friends;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        checkExist(userId);
        checkExist(friendId);
        storage.saveFriend(userId, friendId);
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
    public List<User> getCommonFriends(Integer userId, Integer anotherUserId) {
        checkExist(userId);
        checkExist(anotherUserId);
        List<User> commonFriends = storage.getCommonFriends(userId, anotherUserId);
        log.debug(String.format("Общее количество общих друзей у пользователя %d и пользователя %d: %d", userId, anotherUserId,
                commonFriends.size()));
        return commonFriends;
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    private void checkExist(Integer id) {
        if (!storage.containsInStorage(id)) {
            throw new NotFoundException(String.format("Пользователя с %d нет в списке", id));
        }
    }
}
