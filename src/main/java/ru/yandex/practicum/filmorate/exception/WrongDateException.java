package ru.yandex.practicum.filmorate.exception;

public class WrongDateException extends RuntimeException {

    public WrongDateException(String message) {
        super(message);
    }
}