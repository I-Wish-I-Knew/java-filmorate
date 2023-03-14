package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User get(Long id);

    List<User> getAll();

    boolean containsInStorage(Long id);

    User save(User user);

    User update(User user);

    void delete(Long id);

    List<User> getFriendsByUser(Long id);

    void saveFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> getCommonFriends(Long user1Id, Long user2Id);
}
