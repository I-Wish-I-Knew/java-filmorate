package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DataNotExistException;
import ru.yandex.practicum.filmorate.models.AppData;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class Controller<T extends AppData> {

    protected Map<Integer, T> data = new LinkedHashMap<>();
    protected int id = 0;

    @GetMapping
    public List<T> getAll() {
        log.debug("Количество объектов: {}", data.size());
        return new ArrayList<>(data.values());
    }

    @PostMapping
    public T add(@RequestBody @Valid T t) {
        validate(t);
        t.setId(getId());
        log.debug("Был добавлен объект: " + t);
        data.put(t.getId(), t);
        return t;
    }

    @PutMapping
    public T update(@RequestBody @Valid T t) {
        if (!data.containsKey(t.getId())) {
            RuntimeException e = new DataNotExistException("Отсутствует объект с переданным ID");
            log.error(e.getMessage());
            throw e;
        }
        validate(t);
        log.debug("Был обновлён объект: " + t);
        data.put(t.getId(), t);
        return t;
    }

    protected int getId() {
        return ++id;
    }

    protected abstract void validate(T t);
}
