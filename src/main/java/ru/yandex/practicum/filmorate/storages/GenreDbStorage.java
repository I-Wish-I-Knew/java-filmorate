package ru.yandex.practicum.filmorate.storages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

@Repository
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre get(Integer id) {
        sql = "SELECT * FROM GENRES WHERE GENRE_ID=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new GenreRowMapper());
    }

    public List<Genre> getAll() {
        sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }
}
