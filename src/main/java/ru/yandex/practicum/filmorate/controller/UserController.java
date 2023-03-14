package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("{userId}")
    public User get(@PathVariable Long userId) {
        User user = service.get(userId);
        log.info("Get user {}", user.getId());
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        List<User> users = service.getAll();
        log.info("Get all {} users", users.size());
        return users;
    }

    @PostMapping
    public User add(@RequestBody @Valid User user) {
        User savedUser = service.add(user);
        log.info("User {} was saved", user);
        return savedUser;
    }

    @PutMapping
    public User update(@RequestBody @Validated(Update.class) User user) {
        User updatedUser = service.update(user);
        log.info("User {} was saved", user);
        return updatedUser;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
        log.info("User {} was deleted", userId);
    }

    @PutMapping(value = "{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.addFriend(id, friendId);
        log.info("A friend {} was added to user {} friend list", friendId, id);
        return service.get(id);
    }

    @DeleteMapping(value = "{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.deleteFriend(id, friendId);
        log.info("A friend {} was deleted from user {} friend list", friendId, id);
        return service.get(id);
    }

    @GetMapping(value = "{id}/friends")
    public List<User> getUserFriends(@PathVariable Long id) {
        List<User> friends = service.getUserFriends(id);
        log.info("Get friends for user {}", id);
        return friends;
    }

    @GetMapping(value = "{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        List<User> commonFriends = service.getCommonFriends(id, otherId);
        log.info("Get common friends for users {} and {}", id, otherId);
        return commonFriends;
    }
}
