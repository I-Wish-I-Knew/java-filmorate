package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class User extends AppData {
    @NotBlank(message = "Укажите email")
    @Email(message = "Укажите корректный email")
    private String email;
    @NotBlank(message = "Укажите логин")
    @Pattern(regexp = ("\\S+"), message = "Укажите логин без пробелов")
    private String login;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Дата рождения должна быть в прошлом.")
    private LocalDate birthday;
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();
    private String name;

    public User(Integer id, String email, String login, LocalDate birthday, String name) {
        super(id);
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = name;
    }

}
