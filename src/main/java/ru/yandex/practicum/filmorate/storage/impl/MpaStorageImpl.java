package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaRawMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class MpaStorageImpl implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public MpaStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa get(Integer id) {
        sql = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, new MpaRawMapper(), id);
    }

    public List<Mpa> getAll() {
        sql = "SELECT mpa_id, mpa_name FROM mpa";
        return jdbcTemplate.query(sql, new MpaRawMapper());
    }

    public boolean containsInStorage(Integer id) {
        sql = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, ResultSet::next, id));
    }
}
