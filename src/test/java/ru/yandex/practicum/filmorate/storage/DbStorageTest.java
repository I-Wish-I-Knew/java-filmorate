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
import ru.yandex.practicum.filmorate.storage.impl.GenreStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.MpaStorageImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorageImpl genreStorageImpl;
    private final MpaStorageImpl mpaStorageImpl;

    @Test
    @Order(1)
    void testSaveFilm() {
        assertThat(filmStorage.getAll()).isEmpty();

        Film film = new Film(1L, "new film", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 1);
        filmStorage.save(film);

        assertThat(filmStorage.getAll()).hasSize(1);
    }

    @Test
    @Order(2)
    void testGetFilmById() {
        assertThat(filmStorage.get(1L)).isNotNull().hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(3)
    void testUpdateFilm() {
        Film updatedFilm = filmStorage.get(1L);
        updatedFilm.setName("New name");
        filmStorage.update(updatedFilm);

        assertThat(filmStorage.get(1L)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    @Order(4)
    void testSaveLike() {
        User user = new User(8L, "user8@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);
        Film film = filmStorage.get(1L);

        assertThat(filmStorage.loadFilmLikes(film)).isEmpty();

        filmStorage.saveLike(1L, user.getId());

        assertThat(filmStorage.loadFilmLikes(film)).hasSize(1);
    }

    @Test
    @Order(5)
    void testDeleteLike() {
        Film film = filmStorage.get(1L);
        User user = new User(20L, "user20@mail.com", "login", LocalDate.parse("1979-04-17",
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
        Film film2 = new Film(2L, "new film2", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 5);

        Film film3 = new Film(3L, "new film3", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 7);

        User user1000 = new User(1000L, "user1000@mail.com", "user1000", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");

        User user1001 = new User(1001L, "user1001@mail.com", "user1001", LocalDate.parse("1979-04-17",
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
        User user = new User(1L, "user1@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        userStorage.save(user);

        assertThat(userStorage.getAll()).contains(user);
    }

    @Test
    @Order(8)
    void testUpdate() {
        User updatedUser = userStorage.get(1L);
        updatedUser.setName("New name");
        userStorage.update(updatedUser);

        assertThat(userStorage.get(1L)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    @Order(9)
    void testGetById() {
        assertThat(userStorage.get(1L)).isNotNull().hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(10)
    void testSaveFriend() {
        User friend = new User(2L, "user2@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        userStorage.save(friend);
        userStorage.saveFriend(1L, friend.getId());

        assertThat(userStorage.get(1L).getFriends()).contains(friend.getId());
    }

    @Test
    @Order(11)
    void testGetFriendsForUser() {
        User user = userStorage.get(2L);
        User friend1 = new User(3L, "user3@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        User friend2 = new User(4L, "user4@mail.com", "login", LocalDate.parse("1979-04-17",
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
        User user = new User(10L, "user10@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");
        User friend = new User(11L, "user11@mail.com", "login", LocalDate.parse("1979-04-17",
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
        User user = userStorage.get(1L);
        User user2 = userStorage.get(2L);
        User newFriend = new User(5L, "user5@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "newFriend");
        userStorage.save(newFriend);
        userStorage.saveFriend(user.getId(), newFriend.getId());
        userStorage.saveFriend(user2.getId(), newFriend.getId());

        assertThat(userStorage.getCommonFriends(user.getId(), user2.getId())).contains(newFriend).hasSize(1);
    }

    @Test
    @Order(14)
    void testIsFilmExists() {
        Film film = filmStorage.get(1L);
        assertThat(filmStorage.containsInStorage(film.getId())).isTrue();

        Film film2 = new Film(5L, "new film5", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 7);
        assertThat(filmStorage.containsInStorage(film2.getId())).isFalse();
    }

    @Test
    @Order(15)
    void testIsUserExists() {
        User user = userStorage.get(1L);
        assertThat(userStorage.containsInStorage(user.getId())).isTrue();

        User user2 = new User(100L, "user100@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");
        assertThat(userStorage.containsInStorage(user2.getId())).isFalse();
    }

    @Test
    void testIsGenreExists() {
        Genre genre = genreStorageImpl.get(1);
        assertThat(genreStorageImpl.containsInStorage(genre.getId())).isTrue();

        Genre genre1 = new Genre(10, "жанр");
        assertThat(genreStorageImpl.containsInStorage(genre1.getId())).isFalse();
    }

    @Test
    void testIsMpaExists() {
        Mpa mpa = mpaStorageImpl.get(1);
        assertThat(mpaStorageImpl.containsInStorage(mpa.getId())).isTrue();

        Mpa mpa1 = new Mpa(10, "мпа");
        assertThat(mpaStorageImpl.containsInStorage(mpa1.getId())).isFalse();
    }

    @Test
    void testGetGenreById() {
        assertThat(genreStorageImpl.get(1)).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void testGetAllGenres() {
        assertThat(genreStorageImpl.getAll()).hasSize(6);
    }

    @Test
    void testGetMpaById() {
        assertThat(mpaStorageImpl.get(1)).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    void testGetAllMpa() {
        assertThat(mpaStorageImpl.getAll()).hasSize(5);
    }

    @Test
    void testDeleteFilmById() {
        Film filmForDelete = new Film(5000L, "filmForDelete", "description", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), 100, new Mpa(1, "G"), 5);

        filmStorage.save(filmForDelete);

        assertThat(filmStorage.getAll()).contains(filmForDelete);

        filmStorage.delete(filmForDelete.getId());

        assertThat(filmStorage.getAll()).doesNotContain(filmForDelete);
    }

    @Test
    void testDeleteUserById() {
        User userForDelete = new User(5000L, "userForDelete@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");

        userStorage.save(userForDelete);

        assertThat(userStorage.getAll()).contains(userForDelete);

        userStorage.delete(userForDelete.getId());

        assertThat(userStorage.getAll()).doesNotContain(userForDelete);
    }
}
