package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

@Repository
public class DirectorStorageImpl implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director get(Long id) {
        String sql = "SELECT * FROM directors WHERE director_id = ?;";
        return jdbcTemplate.queryForObject(sql, new DirectorRowMapper(), id);
    }

    @Override
    public List<Director> getAll() {
        String sql = "SELECT * FROM directors ORDER BY director_id; ";
        return jdbcTemplate.query(sql, new DirectorRowMapper());
    }

    @Override
    public Director save(Director director) {
        String sql = "INSERT INTO directors(director_name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        String sql = "UPDATE directors SET director_name = ? WHERE director_id = ?;";
        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());
        return director;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM directors where director_id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean containsInStorage(Long id) {
        String sql = "SELECT * FROM directors WHERE director_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, ResultSet::next, id));
    }
}
