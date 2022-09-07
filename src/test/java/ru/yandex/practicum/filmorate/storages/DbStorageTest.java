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
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Test
    @Order(1)
    public void testSaveFilm() {
        assertThat(filmStorage.getAll()).hasSize(0);

        Film film = new Film(1, "new film", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 1);
        filmStorage.save(film);

        assertThat(filmStorage.getAll()).hasSize(1);
    }

    @Test
    @Order(2)
    public void testGetFilmById() {
        assertThat(filmStorage.get(1)).isNotNull().hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(3)
    public void testUpdateFilm() {
        Film updatedFilm = filmStorage.get(1);
        updatedFilm.setName("New name");
        filmStorage.update(updatedFilm);

        assertThat(filmStorage.get(1)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    @Order(4)
    public void testAddLike() {
        User user = new User(8, "user8@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);

        assertThat(filmStorage.get(1).getLikes()).isEmpty();

        filmStorage.addLike(1, user.getId());

        assertThat(filmStorage.get(1).getLikes()).hasSize(1);
    }

    @Test
    @Order(5)
    public void testDeleteLike() {
        Film film = filmStorage.get(1);
        User user = new User(20, "user20@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);
        filmStorage.addLike(film.getId(), user.getId());

        assertThat(filmStorage.get(film.getId()).getLikes()).contains(user.getId());

        filmStorage.deleteLike(film.getId(), user.getId());

        assertThat(filmStorage.get(film.getId()).getLikes()).doesNotContain(user.getId());
    }

    @Test
    @Order(6)
    public void testGetMostPopular() {
        Film film2 = new Film(2, "new film2", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 5);

        Film film3 = new Film(3, "new film3", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 7);

        User user1000 = new User(1000, "user1000@mail.com", "user1000", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");

        User user1001 = new User(1001, "user1001@mail.com", "user1001", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");

        filmStorage.save(film2);
        filmStorage.save(film3);
        userStorage.save(user1000);
        userStorage.save(user1001);
        filmStorage.saveLike(film3.getId(), user1000.getId());
        filmStorage.saveLike(film3.getId(), user1001.getId());


        assertThat(filmStorage.getMostPopular(1)).contains(film3).hasSize(1);
    }

    @Test
    @Order(7)
    public void testSave() {
        User user = new User(1, "user1@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);

        assertThat(userStorage.getAll()).containsValue(user);
    }

    @Test
    @Order(8)
    public void testUpdate() {
        User updatedUser = userStorage.get(1);
        updatedUser.setName("New name");
        userStorage.update(updatedUser);

        assertThat(userStorage.get(1)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    @Order(9)
    public void testGetById() {
        assertThat(userStorage.get(1)).isNotNull().hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(10)
    public void testAddFriend() {
        User friend = new User(2, "user2@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        userStorage.save(friend);
        userStorage.addFriend(1, friend.getId());

        assertThat(userStorage.get(1).getFriends()).contains(friend.getId());
    }

    @Test
    @Order(11)
    public void testGetFriendsForUser() {
        User user = userStorage.get(2);
        User friend1 = new User(3, "user3@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        User friend2 = new User(4, "user4@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        userStorage.save(friend1);
        userStorage.save(friend2);
        userStorage.addFriend(user.getId(), friend1.getId());
        userStorage.addFriend(user.getId(), friend2.getId());

        assertThat(userStorage.getFriendsForUser(user.getId())).contains(friend1, friend2)
                .hasSize(2);

    }

    @Test
    @Order(12)
    public void testDeleteFriend() {
        User user = new User(10, "user10@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");
        User friend = new User(11, "user11@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        userStorage.save(user);
        userStorage.save(friend);
        userStorage.addFriend(user.getId(), friend.getId());

        assertThat(userStorage.getFriendsForUser(user.getId())).contains(friend);
        userStorage.deleteFriend(user.getId(), friend.getId());

        assertThat(userStorage.get(user.getId()).getFriends()).doesNotContain(friend.getId());
    }

    @Test
    @Order(13)
    public void testGetCommonFriends() {
        User user = userStorage.get(1);
        User user2 = userStorage.get(2);
        User newFriend = new User(5, "user5@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "newFriend");
        userStorage.save(newFriend);
        userStorage.addFriend(user.getId(), newFriend.getId());
        userStorage.addFriend(user2.getId(), newFriend.getId());

        assertThat(userStorage.getCommonFriends(user.getId(), user2.getId())).contains(newFriend).hasSize(1);
    }

    @Test
    @Order(14)
    public void testIsFilmExists() {
        Film film = filmStorage.get(1);
        assertThat(filmStorage.isExists(film.getId())).isTrue();

        Film film2 = new Film(5, "new film5", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 7);
        assertThat(filmStorage.isExists(film2.getId())).isFalse();
    }

    @Test
    @Order(15)
    public void testIsUserExists() {
        User user = userStorage.get(1);
        assertThat(userStorage.isExists(user.getId())).isTrue();

        User user2 = new User(100, "user100@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");
        assertThat(userStorage.isExists(user2.getId())).isFalse();
    }

    @Test
    public void testIsGenreExists() {
        Genre genre = genreStorage.get(1);
        assertThat(genreStorage.isExists(genre.getId())).isTrue();

        Genre genre1 = new Genre(10, "жанр");
        assertThat(genreStorage.isExists(genre1.getId())).isFalse();
    }

    @Test
    public void testIsMpaExists() {
        Mpa mpa = mpaStorage.get(1);
        assertThat(mpaStorage.isExists(mpa.getId())).isTrue();

        Mpa mpa1 = new Mpa(10, "мпа");
        assertThat(mpaStorage.isExists(mpa1.getId())).isFalse();
    }

    @Test
    public void testGetGenreById() {
        assertThat(genreStorage.get(1)).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void testGetAllGenres() {
        assertThat(genreStorage.getAll()).hasSize(6);
    }

    @Test
    public void testGetMpaById() {
        assertThat(mpaStorage.get(1)).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testGetAllMpa() {
        assertThat(mpaStorage.getAll()).hasSize(5);
    }

}
