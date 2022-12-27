package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
public class FilmDbStorageImpl implements FilmDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private String sql;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film get(int id) {
        sql = "SELECT film_id, " +
                "film_name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "rate, " +
                "f.mpa_id, " +
                "mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS mpa ON f.mpa_id = mpa.mpa_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new FilmRowMapper());
    }

    @Override
    public List<Film> getAll() {
        sql = "SELECT film_id, " +
                "film_name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "rate, " +
                "f.mpa_id, " +
                "mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS mpa ON f.mpa_id = mpa.mpa_id";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    @Override
    public Film save(Film film) {
        sql = "INSERT INTO films (film_name, description, release_date, duration, rate, mpa_id)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, 0);
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        saveGenres(film);
        saveFilmDirectors(film);
        updateRate(film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        sql = "UPDATE films SET film_name = ?, description = ?, duration = ?," +
                "mpa_id = ?, release_date = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getDuration(),
                film.getMpa().getId(), film.getReleaseDate(), film.getId());
        deleteGenres(film);
        saveGenres(film);
        deleteFilmDirectors(film);
        saveFilmDirectors(film);
        updateRate(film.getId());
        return film;
    }

    @Override
    public void delete(Integer filmId) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public List<Film> getSortedDirectorsFilmsByLikes(int directorId) {
        String sql = "SELECT f.*, m.mpa_name FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_likes fl ON f.film_id = fl.film_id " +
                "LEFT JOIN film_directors fd ON f.film_id = fd.film_id " +
                "WHERE fd.director_id = ? " +
                "ORDER BY f.rate";
        return jdbcTemplate.query(sql, new FilmRowMapper(), directorId);
    }

    @Override
    public List<Film> getSortedDirectorsFilmsByYear(int directorId) {
        String sql = "SELECT f.*, m.mpa_name FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_directors fd ON f.film_id = fd.film_id " +
                "WHERE fd.director_id = ? " +
                "ORDER BY YEAR(f.release_date)";
        return jdbcTemplate.query(sql, new FilmRowMapper(), directorId);
    }

    @Override
    public void saveLike(int filmId, int userId) {
        sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        sql = "DELETE FROM film_likes " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public List<Film> getMostPopular(int count) {
        sql = "SELECT film_id, " +
                "film_name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "rate, " +
                "f.mpa_id, " +
                "mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS mpa ON f.mpa_id = mpa.mpa_id " +
                "ORDER BY rate DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new FilmRowMapper(), count);
    }

    @Override
    public boolean containsInStorage(int filmId) {
        sql = "SELECT * FROM films WHERE film_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, new Object[]{filmId},
                ResultSet::next));
    }

    public List<Integer> loadFilmLikes(Film film) {
        sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"),
                film.getId());
    }

    public List<Genre> loadFilmGenre(Film film) {
        sql = "SELECT fg.genre_id, g.genre_name FROM film_genres AS fg " +
                "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sql, new GenreRowMapper(), film.getId());

    }

    public List<Director> loadFilmDirectors(Film film) {
        sql = "SELECT d.director_id, d.director_name FROM film_directors fd " +
                "LEFT JOIN directors d ON d.director_id = fd.director_id " +
                "WHERE fd.film_id = ? ORDER BY d.director_id";
        return jdbcTemplate.query(sql, new DirectorRowMapper(), film.getId());
    }

    private void saveGenres(Film film) {
        sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        film.getGenres().forEach(genre -> jdbcTemplate.update(sql, film.getId(), genre.getId()));
    }

    private void deleteGenres(Film film) {
        sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    private void saveFilmDirectors(Film film) {
        sql = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";
        film.getDirectors().forEach(director -> jdbcTemplate.update(sql, film.getId(), director.getId()));
    }

    private void deleteFilmDirectors(Film film) {
        sql = "DELETE FROM film_directors WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    private void updateRate(int filmId) {
        sql = "UPDATE films f " +
                "SET f.rate = COALESCE(SELECT COUNT(user_id) " +
                "FROM film_likes " +
                "WHERE film_id = ?, 0) " +
                "WHERE f.film_id = ?";
        jdbcTemplate.update(sql, filmId, filmId);
    }
}
