package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDbStorage extends DataStorage<User> {

    User save(User user);

    User update(User user);

    void delete(Integer userId);

    List<User> getFriendsByUser(int userId);

    void saveFriend(int userId, int friend_id);

    void deleteFriend(int user_id, int friendId);

    List<User> getCommonFriends(int user1Id, int user2Id);
}
