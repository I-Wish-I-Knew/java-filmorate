package ru.yandex.practicum.filmorate.storages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Primary
public class UserDbStorageImpl implements UserDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User get(Integer id) {
        sql = "SELECT * FROM USERS WHERE USER_ID=?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
        loadUserFriends(id).stream().forEach(friend -> user.getFriends().add(friend));
        return user;
    }

    @Override
    public Map<Integer, User> getAll() {
        sql = "SELECT * FROM USERS";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        Map<Integer, User> allUsers = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        allUsers.keySet().forEach(id -> addFriendsForUser(allUsers.get(id), loadUserFriends(id)));
        return allUsers;
    }

    @Override
    public User save(User user) {
        sql = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        deleteFriends(user);
        saveUserFriends(user);
        return user;
    }

    @Override
    public User update(User user) {
        sql = "UPDATE USERS SET user_name=?, email=?, login=?, birthday=? WHERE USER_ID=?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(),
                user.getBirthday(), user.getId());
        deleteFriends(user);
        saveUserFriends(user);
        return user;
    }

    @Override
    public List<User> getFriendsForUser(Integer userId) {
        return loadUserFriends(userId).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = get(userId);
        sql = "INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
        user.getFriends().add(friendId);
        update(user);
        return get(userId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = get(userId);
        sql = "DELETE from USER_FRIENDS where USER_ID=? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        user.getFriends().remove(friendId);
        update(user);
        return user;
    }

    @Override
    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        sql = "SELECT U1.FRIEND_ID FROM USER_FRIENDS AS U1 JOIN USER_FRIENDS AS U2" +
                " ON U1.FRIEND_ID = U2.FRIEND_ID WHERE U1.USER_ID=? AND U2.USER_ID=?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        rs.getInt("friend_id"), user1Id, user2Id).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    private List<Integer> loadUserFriends(Integer id) {
        sql = "SELECT FRIEND_ID FROM USER_FRIENDS WHERE USER_ID=?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                rs.getInt("friend_id"), id);
    }

    private void addFriendsForUser(User user, List<Integer> friends) {
        friends.forEach(friend -> user.getFriends().add(friend));
    }

    private Set<Integer> saveUserFriends(User user) {
        sql = "INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID) values (?, ?)";
        user.getFriends().forEach(friend -> jdbcTemplate.update(sql, user.getId(), friend));
        return user.getFriends();
    }

    private void deleteFriends(User user) {
        sql = "delete from USER_FRIENDS where USER_ID = ?";
        jdbcTemplate.update(sql, user.getId());
    }
}
