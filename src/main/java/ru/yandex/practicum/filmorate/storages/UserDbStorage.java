package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserDbStorage extends DataStorage<User> {
    List<User> getFriendsForUser(Integer userId);

    User addFriend(Integer userId, Integer friend_id);

    User deleteFriend(Integer user_id, Integer friendId);

    List<User> getCommonFriends(Integer user1Id, Integer user2Id);


}
