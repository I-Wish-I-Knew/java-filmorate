package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {

    private final UserDbStorage storage;

    @Test()
    @Order(1)
    public void testSave() {
        User user = new User(1, "user1@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "name");
        storage.save(user);

        assertThat(storage.getAll()).containsValue(user);
    }

    @Test
    @Order(2)
    public void testUpdate() {
        User updatedUser = storage.get(1);
        updatedUser.setName("New name");
        storage.update(updatedUser);

        assertThat(storage.get(1)).hasFieldOrPropertyWithValue("name", "New name");
    }

    @Test
    @Order(3)
    public void testGetById() {
        assertThat(storage.get(1)).isNotNull().hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(4)
    public void testAddFriend() {
        User friend = new User(2, "user2@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        storage.save(friend);
        storage.addFriend(1, friend.getId());

        assertThat(storage.get(1).getFriends()).contains(friend.getId());
    }

    @Test
    @Order(5)
    public void testGetFriendsForUser() {
        User user = storage.get(2);
        User friend1 = new User(3, "user3@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        User friend2 = new User(4, "user4@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        storage.save(friend1);
        storage.save(friend2);
        storage.addFriend(user.getId(), friend1.getId());
        storage.addFriend(user.getId(), friend2.getId());

        assertThat(storage.getFriendsForUser(user.getId())).contains(friend1, friend2)
                .hasSize(2);

    }

    @Test
    @Order(6)
    public void testDeleteFriend() {
        User user = new User(10, "user10@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "user");
        User friend =  new User(11, "user11@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "friend");
        storage.save(user);
        storage.save(friend);
        storage.addFriend(user.getId(), friend.getId());

        assertThat(storage.getFriendsForUser(user.getId())).contains(friend);
        storage.deleteFriend(user.getId(), friend.getId());

        assertThat(storage.get(user.getId()).getFriends()).doesNotContain(friend.getId());
    }

    @Test
    @Order(7)
    public void testGetCommonFriends() {
        User user = storage.get(1);
        User user2 = storage.get(2);

        User newFriend = new User(5, "user5@mail.com", "login", LocalDate.parse("1979-04-17",
                DateTimeFormatter.ISO_DATE), "newFriend");
        storage.save(newFriend);
        storage.addFriend(user.getId(), newFriend.getId());
        storage.addFriend(user2.getId(), newFriend.getId());

        assertThat(storage.getCommonFriends(user.getId(), user2.getId())).contains(newFriend).hasSize(1);
    }
}
