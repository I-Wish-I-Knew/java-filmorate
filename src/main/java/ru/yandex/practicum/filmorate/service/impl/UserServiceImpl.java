package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Autowired
    public UserServiceImpl(UserStorage storage) {
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
        return storage.getAll();
    }

    @Override
    public User get(Long id) {
        checkExist(id);
        return storage.get(id);
    }

    public void delete(Long id) {
        checkExist(id);
        storage.delete(id);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        checkExist(id);
        return storage.getFriendsByUser(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        checkExist(userId);
        checkExist(friendId);
        storage.saveFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        checkExist(userId);
        checkExist(friendId);
        storage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long anotherUserId) {
        checkExist(userId);
        checkExist(anotherUserId);
        return storage.getCommonFriends(userId, anotherUserId);
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    private void checkExist(Long id) {
        if (!storage.containsInStorage(id)) {
            throw new NotFoundException(String.format("User %d was not found", id));
        }
    }
}
