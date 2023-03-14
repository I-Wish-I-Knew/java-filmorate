package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class UserStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public UserStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User get(Long id) {
        sql = "SELECT user_id, " +
                "user_name, " +
                "email, " +
                "login, " +
                "birthday " +
                "FROM users WHERE user_id = ?";
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        if (user != null) user.getFriends().addAll(loadFriends(id));
        return user;
    }

    @Override
    public List<User> getAll() {
        sql = "SELECT user_id, " +
                "user_name, " +
                "email, " +
                "login, " +
                "birthday " +
                "FROM users";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        users.forEach(user -> user.getFriends().addAll(loadFriends(user.getId())));
        return users;
    }

    @Override
    public User save(User user) {
        sql = "INSERT INTO USERS (email, login, user_name, birthday) VALUES (?, ?, ?, ?)";
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
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        sql = "UPDATE users SET user_name = ?, email = ?, login = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(),
                user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean containsInStorage(Long userId) {
        sql = "SELECT user_id, " +
                "user_name, " +
                "email, " +
                "login, " +
                "birthday " +
                "FROM users WHERE user_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, ResultSet::next, userId));
    }

    @Override
    public List<User> getFriendsByUser(Long userId) {
        return loadFriends(userId).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    @Override
    public void saveFriend(Long userId, Long friendId) {
        sql = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        sql = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        sql = "SELECT u1.friend_id FROM user_friends AS u1 " +
                "INNER JOIN user_friends AS u2 ON u1.friend_id = u2.friend_id " +
                "WHERE u1.user_id = ? AND u2.user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        rs.getLong("friend_id"), user1Id, user2Id).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    private List<Long> loadFriends(Long id) {
        sql = "SELECT friend_id FROM user_friends WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                rs.getLong("friend_id"), id);
    }
}


