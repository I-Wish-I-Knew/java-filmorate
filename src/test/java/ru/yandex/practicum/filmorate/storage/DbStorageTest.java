package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Test
    @Order(1)
    void testSaveFilm() {
        assertThat(filmStorage.getAll()).hasSize(0);

        Film film = new Film(1, "new film", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 1);
        filmStorage.save(film);

        assertThat(filmStorage.getAll()).hasSize(1);
    }

    @Test
    @Order(2)
    void testGetFilmById() {
        assertThat(filmStorage.get(1)).isNotNull().hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(3)
    void testUpdateFilm() {
        Film updatedFilm = filmStorage.get(1);
        updatedFilm.setName("New name");
        filmStorage.update(updatedFilm);

        assertThat(filmStorage.get(1)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    @Order(4)
    void testSaveLike() {
        User user = new User(8, "user8@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);
        Film film = filmStorage.get(1);

        assertThat(filmStorage.loadFilmLikes(film)).isEmpty();

        filmStorage.saveLike(1, user.getId());

        assertThat(filmStorage.loadFilmLikes(film)).hasSize(1);
    }

    @Test
    @Order(5)
    void testDeleteLike() {
        Film film = filmStorage.get(1);
        User user = new User(20, "user20@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);
        filmStorage.saveLike(film.getId(), user.getId());

        assertThat(filmStorage.loadFilmLikes(film)).contains(user.getId());

        filmStorage.deleteLike(film.getId(), user.getId());

        assertThat(filmStorage.loadFilmLikes(film)).doesNotContain(user.getId());
    }

    @Test
    @Order(6)
    void testGetMostPopular() {
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
    void testSave() {
        User user = new User(1, "user1@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);

        assertThat(userStorage.getAll()).contains(user);
    }

    @Test
    @Order(8)
    void testUpdate() {
        User updatedUser = userStorage.get(1);
        updatedUser.setName("New name");
        userStorage.update(updatedUser);

        assertThat(userStorage.get(1)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    @Order(9)
    void testGetById() {
        assertThat(userStorage.get(1)).isNotNull().hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(10)
    void testSaveFriend() {
        User friend = new User(2, "user2@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        userStorage.save(friend);
        userStorage.saveFriend(1, friend.getId());

        assertThat(userStorage.get(1).getFriends()).contains(friend.getId());
    }

    @Test
    @Order(11)
    void testGetFriendsForUser() {
        User user = userStorage.get(2);
        User friend1 = new User(3, "user3@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        User friend2 = new User(4, "user4@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        userStorage.save(friend1);
        userStorage.save(friend2);
        userStorage.saveFriend(user.getId(), friend1.getId());
        userStorage.saveFriend(user.getId(), friend2.getId());

        assertThat(userStorage.getFriendsByUser(user.getId())).contains(friend1, friend2)
                .hasSize(2);
    }

    @Test
    @Order(12)
    void testDeleteFriend() {
        User user = new User(10, "user10@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");
        User friend = new User(11, "user11@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        userStorage.save(user);
        userStorage.save(friend);
        userStorage.saveFriend(user.getId(), friend.getId());

        assertThat(userStorage.getFriendsByUser(user.getId())).contains(friend);
        userStorage.deleteFriend(user.getId(), friend.getId());

        assertThat(userStorage.get(user.getId()).getFriends()).doesNotContain(friend.getId());
    }

    @Test
    @Order(13)
    void testGetCommonFriends() {
        User user = userStorage.get(1);
        User user2 = userStorage.get(2);
        User newFriend = new User(5, "user5@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "newFriend");
        userStorage.save(newFriend);
        userStorage.saveFriend(user.getId(), newFriend.getId());
        userStorage.saveFriend(user2.getId(), newFriend.getId());

        assertThat(userStorage.getCommonFriends(user.getId(), user2.getId())).contains(newFriend).hasSize(1);
    }

    @Test
    @Order(14)
    void testIsFilmExists() {
        Film film = filmStorage.get(1);
        assertThat(filmStorage.containsInStorage(film.getId())).isTrue();

        Film film2 = new Film(5, "new film5", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 7);
        assertThat(filmStorage.containsInStorage(film2.getId())).isFalse();
    }

    @Test
    @Order(15)
    void testIsUserExists() {
        User user = userStorage.get(1);
        assertThat(userStorage.containsInStorage(user.getId())).isTrue();

        User user2 = new User(100, "user100@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");
        assertThat(userStorage.containsInStorage(user2.getId())).isFalse();
    }

    @Test
    void testIsGenreExists() {
        Genre genre = genreStorage.get(1);
        assertThat(genreStorage.containsInStorage(genre.getId())).isTrue();

        Genre genre1 = new Genre(10, "жанр");
        assertThat(genreStorage.containsInStorage(genre1.getId())).isFalse();
    }

    @Test
    void testIsMpaExists() {
        Mpa mpa = mpaStorage.get(1);
        assertThat(mpaStorage.containsInStorage(mpa.getId())).isTrue();

        Mpa mpa1 = new Mpa(10, "мпа");
        assertThat(mpaStorage.containsInStorage(mpa1.getId())).isFalse();
    }

    @Test
    void testGetGenreById() {
        assertThat(genreStorage.get(1)).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void testGetAllGenres() {
        assertThat(genreStorage.getAll()).hasSize(6);
    }

    @Test
    void testGetMpaById() {
        assertThat(mpaStorage.get(1)).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    void testGetAllMpa() {
        assertThat(mpaStorage.getAll()).hasSize(5);
    }

    @Test
    void testDeleteFilmById() {
        Film filmForDelete = new Film(5000, "filmForDelete", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 5);

        filmStorage.save(filmForDelete);

        assertThat(filmStorage.getAll()).contains(filmForDelete);

        filmStorage.delete(filmForDelete.getId());

        assertThat(filmStorage.getAll()).doesNotContain(filmForDelete);
    }

    @Test
    void testDeleteUserById() {
        User userForDelete = new User(5000, "userForDelete@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");

        userStorage.save(userForDelete);

        assertThat(userStorage.getAll()).contains(userForDelete);

        userStorage.delete(userForDelete.getId());

        assertThat(userStorage.getAll()).doesNotContain(userForDelete);
    }
}
