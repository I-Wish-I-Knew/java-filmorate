package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("{userId}")
    public User getUser(@PathVariable int userId) {
        return service.get(userId);
    }

    @GetMapping
    public List<User> getAll() {
        log.debug(String.format("Общее количество пользователей: %d", service.getAll().size()));
        return service.getAll();
    }

    @PostMapping
    public User add(@RequestBody @Valid User user) {
        validateEmail(user);
        setName(user);
        User createdUser = service.add(user);
        log.debug("Был добавлен пользователь: " + createdUser);
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        setName(user);
        User updatedUser = service.update(user);
        log.debug("Был обновлён пользователь: " + updatedUser);
        return updatedUser;
    }

    @PutMapping(value = "{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        service.addFriend(id, friendId);
        log.debug(String.format("Пользователи %d и %d стали друзьями", id, friendId));
        return service.get(id);
    }

    @DeleteMapping(value = "{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        service.deleteFriend(id, friendId);
        log.debug(String.format("Пользователи %d и %d больше не друзья", id, friendId));
        return service.get(id);
    }

    @GetMapping(value = "{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        log.debug(String.format("Общее количество друзей у пользователя %d: %d", id,
                service.getUserFriends(id).size()));
        return service.getUserFriends(id);
    }

    @GetMapping(value = "{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.debug(String.format("Общее количество общих друзей у пользователя %d и пользователя %d: %d", id, otherId,
                service.getCommonFriends(id, otherId).size()));
        return service.getCommonFriends(id, otherId);
    }

    private void validateEmail(User user) {
        List<User> users = service.getAll();
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
