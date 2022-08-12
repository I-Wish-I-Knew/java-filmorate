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
public class MpaStorageTest {

    private final MpaDbStorage storage;

    @Test
    public void testGetMpaById() {
        assertThat(storage.get(1)).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testGetAllMpa() {
        assertThat(storage.getAll()).hasSize(5);
    }
}
