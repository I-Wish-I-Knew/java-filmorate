package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable int userId) {
        return service.get(userId);
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @PostMapping
    public User add(@RequestBody @Valid User user) {
        return service.add(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        return service.update(user);
    }

    @PutMapping(value = "{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        service.addFriend(id, friendId);
        return service.get(id);
    }

    @DeleteMapping(value = "{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        service.deleteFriend(id, friendId);
        return service.get(id);
    }

    @GetMapping(value = "{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        return service.getUserFriends(id);
    }

    @GetMapping(value = "{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return service.getCommonFriends(id, otherId);
    }
}
