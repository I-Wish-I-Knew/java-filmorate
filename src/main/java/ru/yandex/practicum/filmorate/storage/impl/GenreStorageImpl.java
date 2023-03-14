package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class GenreStorageImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public GenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre get(Integer id) {
        sql = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sql, new GenreRowMapper(), id);
    }

    public List<Genre> getAll() {
        sql = "SELECT genre_id, genre_name FROM genres";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    public boolean containsInStorage(Integer id) {
        sql = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, ResultSet::next, id));
    }
}
