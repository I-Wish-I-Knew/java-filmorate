package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserDbStorage extends DataStorage<User> {

    User save(User user);

    User update(User user);

    List<User> getFriendsForUser(int userId);

    void addFriend(int userId, int friend_id);

    void deleteFriend(int user_id, int friendId);

    List<User> getCommonFriends(int user1Id, int user2Id);

}
