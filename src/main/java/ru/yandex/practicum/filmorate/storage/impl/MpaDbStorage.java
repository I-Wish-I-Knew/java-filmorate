package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaRawMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DataStorage;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class MpaDbStorage implements DataStorage<Mpa> {
    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa get(int id) {
        sql = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new MpaRawMapper());
    }

    public List<Mpa> getAll() {
        sql = "SELECT mpa_id, mpa_name FROM mpa";
        return jdbcTemplate.query(sql, new MpaRawMapper());
    }

    public boolean containsInStorage(int mpaId) {
        sql = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, new Object[]{mpaId},
                ResultSet::next));
    }
}
