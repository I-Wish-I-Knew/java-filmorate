package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("При попытке сохранить пользователя с пустым email сервер возвращает ошибку")
    void shouldNotSaveUserWhenUserEmailIsEmpty() throws Exception {
        String userGson = "{\"email\":\"\","
                + "\"login\":\"login\","
                + "\"name\":\"name\","
                + "\"birthday\":\"1967-03-25\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("При попытке сохранить пользователя c email null сервер возвращает ошибку")
    void shouldNotSaveUserWhenUserEmailIsNull() throws Exception {
        String userGson = "{\"email\":null,"
                + "\"login\":\"login\","
                + "\"name\":\"name\","
                + "\"birthday\":\"1967-03-25\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("При попытке сохранить пользователя c логином содержащим пробелы сервер возвращает ошибку")
    void shouldNotSaveUserWhenUserLoginContainsWhitespaces() throws Exception {
        String userGson = "{\"email\":\"email@email.com\","
                + "\"login\":\"wrong login\","
                + "\"name\":\"name\","
                + "\"birthday\":\"1967-03-25\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("При попытке сохранить пользователя c логином null сервер возвращает ошибку")
    void shouldNotSaveUserWhenUserLoginIsNull() throws Exception {
        String userGson = "{\"email\":\"email@email.com\","
                + "\"login\":null,"
                + "\"name\":\"name\","
                + "\"birthday\":\"1967-03-25\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("При попытке сохранить пользователя c пустым логином сервер возвращает ошибку")
    void shouldNotSaveUserWhenUserLoginIsEmpty() throws Exception {
        String userGson = "{\"email\":\"email@email.com\","
                + "\"login\":\"\","
                + "\"name\":\"name\","
                + "\"birthday\":\"1967-03-25\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Если имя пустое, вместо имени используется логин")
    void shouldUseLoginForNameWhenNameIsEmpty() throws Exception {
        String userGson = "{\"email\":\"email@email.com\","
                + "\"login\":\"login\","
                + "\"name\":\"\","
                + "\"birthday\":\"1967-03-25\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userGson))
                .andExpect(jsonPath("$.name").value("login"));
    }

    @Test
    @DisplayName("При попытке сохранить пользователя c датой рождения в будущем сервер возвращает ошибку")
    void shouldNotSaveUserWhenUserBirthdayIsInFuture() throws Exception {
        String userGson = "{\"email\":\"email@email.com\","
                + "\"login\":\"login\","
                + "\"name\":\"login\","
                + "\"birthday\":\"2023-03-25\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userGson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("При попытке обновить пользователя, которого нет в списке сервер возвращает ошибку")
    void shouldNotUpdateUserWhenUserNotExist() throws Exception {
        String userGson = "{\"email\":\"email@email.com\","
                + "\"login\":\"login\","
                + "\"name\":\"name\","
                + "\"birthday\":\"2021-03-26\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userGson))
                .andExpect(status().isNotFound());
    }
}