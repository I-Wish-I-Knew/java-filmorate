package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.DataStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<User> implements UserService {

    @Autowired
    public UserServiceImpl(DataStorage<User> storage) {
        super(storage);
    }

    @Override
    public User add(User user) {
        setName(user);
        return super.add(user);
    }

    public User update(User user) {
        setName(user);
        return super.update(user);
    }

    public List<User> getUserFriends(Integer userId) {
        checkExist(userId);
        List<User> friends = storage.getAll().get(userId).getFriends().stream()
                .map(storage::get)
                .collect(Collectors.toList());
        log.debug(String.format("Общее количество друзей у пользователя %d: %d", userId,
                friends.size()));
        return friends;
    }

    public User addFriend(Integer userId, Integer friendId) {
        checkExist(userId);
        checkExist(friendId);
        storage.getAll().get(userId).getFriends().add(friendId);
        storage.getAll().get(friendId).getFriends().add(userId);
        log.debug(String.format("Пользователи %d и %d стали друзьями", userId, friendId));
        return storage.getAll().get(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        checkExist(userId);
        checkExist(friendId);
        User user = storage.getAll().get(userId);
        User friend = storage.getAll().get(friendId);
        storage.getAll().get(userId).getFriends().remove(friend);
        storage.getAll().get(friendId).getFriends().remove(user);
        log.debug(String.format("Пользователи %d и %d больше не друзья", userId, friendId));
        return user;
    }

    public List<User> getCommonFriends(Integer userId, Integer user2Id) {
        super.checkExist(userId);
        super.checkExist(user2Id);
        Set<Integer> userFriends = storage.getAll().get(userId).getFriends();
        Set<Integer> user2Friends = storage.getAll().get(user2Id).getFriends();
        List<User> commonFriends = userFriends.stream()
                .filter(user2Friends::contains)
                .map(storage::get)
                .collect(Collectors.toList());
        log.debug(String.format("Общее количество общих друзей у пользователя %d и пользователя %d: %d", userId, user2Id,
                commonFriends.size()));
        return commonFriends;
    }

    protected void validate(User user) {
        List<User> users = getAll();
        if (users.stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            RuntimeException e = new DataAlreadyExistException("Пользователь с указанным адресом электронной почты " +
                    "уже был добавлен ранее");
            log.error(e.getMessage());
            throw e;
        }
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
