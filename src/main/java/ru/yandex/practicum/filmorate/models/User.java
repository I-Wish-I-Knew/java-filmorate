package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User extends AppData {
    @NotBlank(message = "Укажите email")
    @Email(message = "Укажите корректный email")
    private final String email;
    @NotBlank(message = "Укажите логин")
    @Pattern(regexp = ("\\S+"), message = "Укажите логин без пробелов")
    private final String login;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Дата рождения должна быть в прошлом.")
    private final LocalDate birthday;
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();
    private String name;
}
