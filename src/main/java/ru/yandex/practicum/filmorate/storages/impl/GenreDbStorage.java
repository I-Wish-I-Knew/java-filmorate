package ru.yandex.practicum.filmorate.storages.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storages.DataStorage;

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
        sql = "SELECT * FROM GENRES WHERE GENRE_ID=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new GenreRowMapper());
    }

    public List<Genre> getAll() {
        sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    public boolean isExists(int genreId) {
        sql = "SELECT * FROM GENRES WHERE GENRE_ID=?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, new Object[]{genreId},
                ResultSet::next));
    }
}
