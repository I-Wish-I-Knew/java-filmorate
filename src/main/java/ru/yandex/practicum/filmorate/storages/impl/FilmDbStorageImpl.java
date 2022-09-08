package ru.yandex.practicum.filmorate.storages.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storages.FilmDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

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
        Film film = jdbcTemplate.queryForObject(sql, new Object[]{id}, new FilmRowMapper());
        film.getGenres().addAll(loadFilmGenre(film));
        film.getLikes().addAll(loadFilmLikes(film));
        return film;
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
        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper());
        films.forEach(film -> film.getGenres().addAll(loadFilmGenre(film)));
        films.forEach(film -> film.getLikes().addAll(loadFilmLikes(film)));
        return films;
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
        film.setId(keyHolder.getKey().intValue());
        saveGenres(film);
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
        return film;
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
        List<Film> popularFilms = jdbcTemplate.query(sql, new FilmRowMapper(), count);
        popularFilms.forEach(film -> film.getGenres().addAll(loadFilmGenre(film)));
        popularFilms.forEach(film -> film.getLikes().addAll(loadFilmLikes(film)));
        return popularFilms;
    }

    @Override
    public boolean containsInStorage(int filmId) {
        sql = "SELECT * FROM films WHERE film_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, new Object[]{filmId},
                ResultSet::next));
    }

    @Override
    public void deleteFilmById(Integer filmId) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private List<Integer> loadFilmLikes(Film film) {
        sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), film.getId());
    }

    private List<Genre> loadFilmGenre(Film film) {
        sql = "SELECT fg.genre_id, g.genre_name FROM film_genres AS fg " +
                "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")
        ), film.getId());

    }

    private void saveGenres(Film film) {
        sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        film.getGenres().forEach(genre -> jdbcTemplate.update(sql, film.getId(), genre.getId()));
    }

    private void deleteGenres(Film film) {
        sql = "DELETE FROM film_genres WHERE film_id = ?";
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
