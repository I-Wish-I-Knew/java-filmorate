package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements DataStorage<User> {
    private final Map<Integer, User> users;
    private int id = 0;

    public InMemoryUserStorage() {
        users = new LinkedHashMap<>();
    }

    public Map<Integer, User> getAll() {
        return users;
    }

    public User add(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User get(Integer userId) {
        return users.get(userId);
    }

    private int getId() {
        return ++id;
    }
}
