package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("При попытке сохраненить фильм без названия сервер возвращает ошибку")
    void shouldNotSaveFilmWhenFilmsNameIsEmpty() throws Exception {
        String filmGson = "{\"name\":\"\","
                + "\"description\":\"description\","
                + "\"releaseDate\":\"1967-03-25\","
                + "\"duration\":100,"
                + "\"mpa\": {\"id\":1}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("При попытке сохраненить фильм без названия сервер возвращает ошибку")
    void shouldNotSaveFilmWhenFilmsNameIsNull() throws Exception {
        String filmGson = "{\"name\":null,"
                + "\"description\":\"description\","
                + "\"releaseDate\":\"1967-03-25\","
                + "\"duration\":100,"
                + "\"mpa\": {\"id\":1}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("При попытке сохраненить фильм с описанием длиннее 200 символов сервер возвращает ошибку")
    void shouldNotSaveFilmWhenFilmsDescriptionIsLongerThan200() throws Exception {
        String filmGson = "{\"name\":\"Another James Bond\","
                + "\"description\":\"Who is James Bond? James Bond is a British literary and film character. " +
                "He is often depicted as a peerless spy, notorious womanizer, and masculine icon. " +
                "He is also designated as agent 007 (always artic\","
                + "\"releaseDate\":\"1967-03-25\","
                + "\"duration\":100,"
                + "\"mpa\": {\"id\":1}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Фильм с описанием равным 200 символов успешно сохраняется")
    void shouldSaveFilmWhenFilmsDescriptionIsNotLongerThan200() throws Exception {
        String filmGson = "{\"name\":\"James Bond\","
                + "\"description\":\"Who is James Bond? James Bond is a British literary and film character. " +
                "He is often depicted as a peerless spy, notorious womanizer, and masculine icon. " +
                "He is also designated as agent 007 (always art\","
                + "\"releaseDate\":\"1967-03-25\","
                + "\"duration\":100,"
                + "\"mpa\": {\"id\":1}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmGson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("При попытке сохраненить фильм с датой выпуска 28.12.1895 сервер возвращает ошибку")
    void shouldNotSaveFilmWhenFilmsReleaseDateIsBefore28_12_1895() throws Exception {
        String filmGson = "{\"name\":\"James Bond\","
                + "\"description\":\"description\","
                + "\"releaseDate\":\"1895-12-27\","
                + "\"duration\":100,"
                + "\"mpa\": {\"id\":1}}";
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Фильм с датой выпуска 29.12.1895 успешно сохраняется")
    void shouldSaveFilmWhenFilmsReleaseDateIsNotBefore29_12_1895() throws Exception {
        String filmGson = "{\"name\":\"James Bond\","
                + "\"description\":\"description\","
                + "\"releaseDate\":\"1895-12-29\","
                + "\"duration\":100,"
                + "\"mpa\": { \"id\":1}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmGson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("При попытке сохраненить фильм с продолжительностью меньше 0 сервер возвращает ошибку")
    void shouldNotSaveFilmWhenFilmsDurationIsLessThan0() throws Exception {
        String filmGson = "{\"name\":\"James Bond\","
                + "\"description\":\"description\","
                + "\"releaseDate\":\"1895-12-28\","
                + "\"duration\":-1},"
                + "\"mpa\": {\"id\":1}";


        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("При попытке обновить фильм, которого нет в списке сервер возвращает ошибку")
    void shouldNotUpdateFilmWhenFilmNotExist() throws Exception {
        String filmGson = "{\"name\":\"James Bond\","
                + "\"description\":\"description\","
                + "\"releaseDate\":\"1895-12-28\","
                + "\"duration\":100,"
                + "\"mpa\": {\"id\":1}}";

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmGson))
                .andExpect(status().isNotFound());
    }
}
