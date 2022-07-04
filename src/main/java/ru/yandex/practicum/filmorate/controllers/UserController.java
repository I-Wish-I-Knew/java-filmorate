package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.DataAlreadyExistException;
import ru.yandex.practicum.filmorate.models.User;

@RequestMapping("/users")
@RestController
public class UserController extends Controller<User> {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    protected void validate(User user) {
        if (data.values().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            RuntimeException e = new DataAlreadyExistException("Пользователь с указанным адресом электронной почты " +
                    "уже был добавлен ранее");
            log.error(e.getMessage());
            throw e;
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
