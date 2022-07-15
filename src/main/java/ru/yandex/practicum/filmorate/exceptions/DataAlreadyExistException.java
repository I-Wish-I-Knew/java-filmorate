package ru.yandex.practicum.filmorate.exceptions;

public class DataAlreadyExistException extends RuntimeException {

    public DataAlreadyExistException(String message) {
        super(message);
    }
}
