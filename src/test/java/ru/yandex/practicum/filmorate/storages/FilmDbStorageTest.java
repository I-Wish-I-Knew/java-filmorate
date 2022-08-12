package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage storage;
    private final UserDbStorage userStorage;

    @Test
    @Order(1)
    public void testSaveFilm() {
        assertThat(storage.getAll().size()).isEqualTo(0);

        Film film = new Film(1, "new film", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 1);
        storage.save(film);

        assertThat(storage.getAll()).hasSize(1);
    }

    @Test
    @Order(2)
    public void testGetFilmById() {
        assertThat(storage.get(1)).isNotNull().hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(3)
    public void testUpdateFilm() {
        Film updatedFilm = storage.get(1);
        updatedFilm.setName("New name");
        storage.update(updatedFilm);

        assertThat(storage.get(1)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    @Order(4)
    public void testAddLike() {
        User user = new User(1, "user@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);

        assertThat(storage.get(1).getLikes()).isEmpty();

        storage.addLike(1, user.getId());

        assertThat(storage.get(1).getLikes()).hasSize(1);
    }

    @Test
    @Order(5)
    public void testDeleteLike() {
        Film film = storage.get(1);
        User user = new User(1, "user@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");

        assertThat(storage.get(1).getLikes()).hasSize(1);

        storage.deleteLike(film.getId(), user.getId());

        assertThat(storage.get(1).getLikes()).isEmpty();
    }

    @Test
    @Order(6)
    public void testGetMostPopular() {
        Film film = new Film(2, "new film2", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 5);

        Film film2 = new Film(3, "new film3", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 7);

        storage.save(film);
        storage.save(film2);

        assertThat(storage.getMostPopular(1)).contains(film2).hasSize(1);
    }

}
