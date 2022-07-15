package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.DataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    private DataStorage<User> storage;

    public User add(User user) {
        storage.add(user);
        return user;
    }

    public User update(User user) {
        checkExist(user.getId());
        storage.update(user);
        return user;
    }

    public User get(Integer id) {
        checkExist(id);
        return storage.get(id);
    }

    public List<User> getAll() {
        return new ArrayList<>(storage.getAll().values());
    }

    public List<User> getUserFriends(Integer userId) {
        checkExist(userId);
        return storage.getAll().get(userId).getFriends().stream()
                .map(id -> storage.get(id))
                .collect(Collectors.toList());
    }

    public User addFriend(Integer userId, Integer friendId) {
        checkExist(userId);
        checkExist(friendId);
        storage.getAll().get(userId).getFriends().add(friendId);
        storage.getAll().get(friendId).getFriends().add(userId);
        return storage.getAll().get(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        checkExist(userId);
        checkExist(friendId);
        User user = storage.getAll().get(userId);
        User friend = storage.getAll().get(friendId);
        storage.getAll().get(userId).getFriends().remove(friend);
        storage.getAll().get(friendId).getFriends().remove(user);
        return user;
    }

    public List<User> getCommonFriends(Integer userId, Integer user2Id) {
        checkExist(userId);
        checkExist(user2Id);
        Set<Integer> userFriends = storage.getAll().get(userId).getFriends();
        Set<Integer> user2Friends = storage.getAll().get(user2Id).getFriends();
        return userFriends.stream()
                .filter(user2Friends::contains)
                .map(id -> storage.get(id))
                .collect(Collectors.toList());
    }

    private void checkExist(Integer id) {
        if (!storage.getAll().containsKey(id)) {
            throw new NotFoundException(String.format("Пользователя с %d нет в списке", id));
        }
    }
}
