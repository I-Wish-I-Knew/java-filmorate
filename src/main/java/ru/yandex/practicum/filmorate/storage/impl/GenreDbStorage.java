package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DataStorage;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class GenreDbStorage implements DataStorage<Genre> {

    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre get(int id) {
        sql = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new GenreRowMapper());
    }

    public List<Genre> getAll() {
        sql = "SELECT genre_id, genre_name FROM genres";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    public boolean containsInStorage(int genreId) {
        sql = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, new Object[]{genreId},
                ResultSet::next));
    }
}
