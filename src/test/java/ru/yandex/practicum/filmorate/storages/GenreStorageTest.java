package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {

    private final GenreDbStorage storage;

    @Test
    public void testGetGenreById() {
        assertThat(storage.get(1)).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void testGetAllGenres() {
        assertThat(storage.getAll()).hasSize(6);
    }
}
