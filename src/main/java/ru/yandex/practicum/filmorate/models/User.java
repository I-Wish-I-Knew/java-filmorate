package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User extends AppData {
    @NotBlank(message = "Укажите email")
    @Email(message = "Укажите корректный email")
    private final String email;
    @NotBlank(message = "Укажите логин")
    @Pattern(regexp = ("\\S+"), message = "Укажите логин без пробелов")
    private final String login;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Дата рождения должна быть в прошлом.")
    private final LocalDate birthday;
}
