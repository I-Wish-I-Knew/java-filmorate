package ru.yandex.practicum.filmorate.storages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Film get(Integer id) {
        sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, F.MPA_ID, MPA_NAME " +
                "FROM FILMS AS F LEFT JOIN MPA AS MPA ON F.MPA_ID=MPA.MPA_ID WHERE FILM_ID=?";
        Film film = jdbcTemplate.queryForObject(sql, new Object[]{id}, new FilmRowMapper());
        loadFilmGenre(film).forEach(genre -> film.getGenres().add(genre));
        loadFilmLikes(film).forEach(like -> film.getLikes().add(like));
        return film;
    }

    @Override
    public Map<Integer, Film> getAll() {
        sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, F.MPA_ID, MPA_NAME" +
                " FROM FILMS AS F LEFT JOIN MPA M ON M.MPA_ID = F.MPA_ID";
        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper());
        Map<Integer, Film> allFilms = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));
        allFilms.values().forEach(this::loadFilmGenre);
        allFilms.values().forEach(this::loadFilmLikes);
        return allFilms;
    }

    @Override
    public Film save(Film film) {
        sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID)" +
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
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        deleteGenresForFilm(film);
        deleteLikesForFilm(film);
        saveGenresForFilm(film);
        saveFilmLikes(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        sql = "UPDATE FILMS SET FILM_NAME=?, DESCRIPTION=?, DURATION=?," +
                "MPA_ID=?, RATE=?, RELEASE_DATE=? WHERE FILM_ID=?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getDuration(),
                film.getMpa().getId(), film.getRate(), film.getReleaseDate(), film.getId());
        deleteGenresForFilm(film);
        deleteLikesForFilm(film);
        saveGenresForFilm(film);
        saveFilmLikes(film);
        return film;
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        Film film = get(filmId);
        film.getLikes().add(userId);
        film.setRate(film.getRate() + 1);
        update(film);
        return film;
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        sql = "DELETE FROM FILM_LIKES WHERE FILM_ID=? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
        Film film = get(filmId);
        film.setRate(film.getRate() - 1);
        update(film);
        return film;
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        sql = String.format("SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, F.MPA_ID, MPA_NAME" +
                " FROM FILMS AS F LEFT JOIN MPA AS MPA ON F.MPA_ID=MPA.MPA_ID " +
                "ORDER BY RATE DESC LIMIT %d", count);
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    @Override
    public boolean isExists(Integer filmId) {
        sql = "SELECT * FROM FILMS WHERE FILM_ID=?";
        return Boolean.TRUE.equals(jdbcTemplate.query(sql, new Object[]{filmId},
                ResultSet::next));
    }

    private List<Integer> loadFilmLikes(Film film) {
        sql = "SELECT USER_ID FROM FILM_LIKES WHERE FILM_ID=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), film.getId());
    }

    private Set<Integer> saveFilmLikes(Film film) {
        sql = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        film.getLikes().forEach(like -> jdbcTemplate.update(sql, film.getId(), like));
        return film.getLikes();
    }

    private List<Genre> loadFilmGenre(Film film) {
        sql = "SELECT FG.GENRE_ID, GENRE_NAME FROM FILM_GENRES AS FG LEFT JOIN GENRES AS G ON " +
                "G.GENRE_ID=FG.GENRE_ID WHERE FILM_ID=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")
        ), film.getId());

    }

    private Set<Genre> saveGenresForFilm(Film film) {
        sql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        film.getGenres().forEach(genre -> jdbcTemplate.update(sql, film.getId(), genre.getId()));
        return film.getGenres();
    }

    private void deleteGenresForFilm(Film film) {
        sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    private void deleteLikesForFilm(Film film) {
        sql = "DELETE FROM FILM_LIKES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }
}
