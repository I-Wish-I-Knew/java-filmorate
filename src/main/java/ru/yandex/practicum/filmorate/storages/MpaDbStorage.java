package ru.yandex.practicum.filmorate.storages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.MpaRawMapper;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;

@Repository
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa get(Integer id) {
        sql = "SELECT * FROM MPA WHERE MPA_ID=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new MpaRawMapper());
    }

    public List<Mpa> getAll() {
        sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, new MpaRawMapper());
    }
}
